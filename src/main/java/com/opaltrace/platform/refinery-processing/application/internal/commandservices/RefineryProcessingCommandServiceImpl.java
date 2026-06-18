package com.opaltrace.platform.refineryprocessing.application.internal.commandservices;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.GpsCoordinate;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.WeightKg;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.refineryprocessing.application.commandservices.RefineryProcessingCommandService;
import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.CompleteProcessingCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.ReceiveBatchAtRefineryCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.SplitBatchCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.entities.SubLotRecord;
import com.opaltrace.platform.refineryprocessing.domain.model.events.BatchDividedEvent;
import com.opaltrace.platform.refineryprocessing.domain.model.events.ChildBatchCreatedEvent;
import com.opaltrace.platform.refineryprocessing.domain.repositories.RefineryReceiptRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class RefineryProcessingCommandServiceImpl implements RefineryProcessingCommandService {

    private final RefineryReceiptRepository refineryReceiptRepository;
    private final MineralBatchRepository mineralBatchRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RefineryProcessingCommandServiceImpl(RefineryReceiptRepository refineryReceiptRepository,
                                                MineralBatchRepository mineralBatchRepository,
                                                ApplicationEventPublisher eventPublisher) {
        this.refineryReceiptRepository = refineryReceiptRepository;
        this.mineralBatchRepository = mineralBatchRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Long, ApplicationError> handle(ReceiveBatchAtRefineryCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.batchPk());
        if (batchOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("MineralBatch", command.batchPk().toString()));
        }
        var batch = batchOpt.get();
        if (batch.isBlocked()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "batch-blocked",
                    "Batch is blocked: " + batch.getBatchId()));
        }
        if (batch.getStatus() != BatchStatus.EN_TRANSITO) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "invalid-status",
                    "Expected EN_TRANSITO, got: " + batch.getStatus()));
        }
        double originalWeightKg = batch.getWeight().value();
        double discrepancy = Math.abs(command.declaredWeightKg() - originalWeightKg) / originalWeightKg * 100.0;
        if (discrepancy > 2.0) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "weight-discrepancy",
                    "Declared %.2fkg vs registered %.2fkg - Discrepancy: %.2f%%".formatted(
                            command.declaredWeightKg(), originalWeightKg, discrepancy)));
        }
        var receipt = new RefineryReceipt(command, originalWeightKg);
        var saved = refineryReceiptRepository.save(receipt);
        batch.transitionStatus(BatchStatus.EN_PLANTA);
        mineralBatchRepository.save(batch);
        return Result.success(saved.getId());
    }

    @Override
    public Result<List<Long>, ApplicationError> handle(SplitBatchCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.parentBatchPk());
        if (batchOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("MineralBatch", command.parentBatchPk().toString()));
        }
        var parentBatch = batchOpt.get();
        if (parentBatch.isBlocked()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "batch-blocked",
                    "Parent batch is blocked: " + parentBatch.getBatchId()));
        }
        if (parentBatch.getStatus() != BatchStatus.EN_PLANTA) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "invalid-status",
                    "Expected EN_PLANTA, got: " + parentBatch.getStatus()));
        }
        double totalSubLotWeight = command.subLots().stream().mapToDouble(SplitBatchCommand.SubLotRequest::weightKg).sum();
        if (totalSubLotWeight > parentBatch.getWeight().value()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "mass-conservation",
                    "Sum %.2fkg exceeds parent %.2fkg".formatted(totalSubLotWeight, parentBatch.getWeight().value())));
        }

        var receiptOpt = refineryReceiptRepository.findByBatchPk(command.parentBatchPk());
        RefineryReceipt receipt = receiptOpt.orElseGet(() -> {
            var r = new RefineryReceipt();
            r.reconstitute(null, parentBatch.getId(), parentBatch.getBatchId(),
                    null, command.supervisorId(),
                    parentBatch.getWeight().value(), parentBatch.getWeight().value(), 0.0,
                    LocalDateTime.now(), null, null, new ArrayList<>());
            return refineryReceiptRepository.save(r);
        });

        List<Long> childBatchPks = new ArrayList<>();
        int year = Year.now().getValue();
        int baseCount = mineralBatchRepository.countByYear(year);

        for (int i = 0; i < command.subLots().size(); i++) {
            var subLotRequest = command.subLots().get(i);
            int seq = baseCount + i + 1;
            String childBatchId = parentBatch.getBatchId() + "-S" + (i + 1);

            var childBatch = new MineralBatch();
            childBatch.reconstitute(
                    childBatchId,
                    subLotRequest.mineralType(),
                    new WeightKg(subLotRequest.weightKg()),
                    parentBatch.getOriginCoordinates(),
                    BatchStatus.EN_PLANTA,
                    false,
                    command.supervisorId(),
                    parentBatch.getMiningCompanyId(),
                    null,
                    parentBatch.getId(),
                    null,
                    new ArrayList<>()
            );
            var savedChild = mineralBatchRepository.save(childBatch);
            childBatchPks.add(savedChild.getId());

            var subLotRecord = new SubLotRecord(parentBatch.getId(), savedChild.getId(), childBatchId, subLotRequest.weightKg());
            receipt.addSubLot(subLotRecord);

            eventPublisher.publishEvent(new ChildBatchCreatedEvent(
                    parentBatch.getBatchId(),
                    parentBatch.getId(),
                    childBatchId,
                    savedChild.getId(),
                    subLotRequest.weightKg(),
                    LocalDateTime.now()
            ));
        }

        refineryReceiptRepository.save(receipt);
        eventPublisher.publishEvent(new BatchDividedEvent(
                parentBatch.getBatchId(),
                parentBatch.getId(),
                command.subLots().size(),
                LocalDateTime.now()
        ));

        return Result.success(childBatchPks);
    }

    @Override
    public Result<Long, ApplicationError> handle(CompleteProcessingCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.batchPk());
        if (batchOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("MineralBatch", command.batchPk().toString()));
        }
        var batch = batchOpt.get();
        if (batch.isBlocked()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "batch-blocked",
                    "Batch is blocked: " + batch.getBatchId()));
        }
        if (batch.getStatus() != BatchStatus.EN_PLANTA) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "invalid-status",
                    "Expected EN_PLANTA, got: " + batch.getStatus()));
        }

        var receiptOpt = refineryReceiptRepository.findByBatchPk(command.batchPk());
        if (receiptOpt.isPresent()) {
            var receipt = receiptOpt.get();
            boolean allSubLotsProcessed = receipt.getSubLots().stream().allMatch(sl -> {
                var childBatch = mineralBatchRepository.findById(sl.getChildBatchPk());
                return childBatch.map(b -> b.getStatus() == BatchStatus.PROCESADO).orElse(true);
            });
            if (!allSubLotsProcessed) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "sub-lots-not-processed",
                        "Not all sub-lots have been processed for batch: " + batch.getBatchId()));
            }
        }

        batch.transitionStatus(BatchStatus.PROCESADO);
        var saved = mineralBatchRepository.save(batch);

        if (receiptOpt.isPresent()) {
            var receipt = receiptOpt.get();
            double shrinkagePercent = receipt.getDeclaredWeightKg() > 0
                    ? Math.abs(receipt.getDeclaredWeightKg() - batch.getWeight().value()) / receipt.getDeclaredWeightKg() * 100.0
                    : 0.0;
            receipt.completeProcessing(shrinkagePercent);
            refineryReceiptRepository.save(receipt);
        }

        return Result.success(saved.getId());
    }
}
