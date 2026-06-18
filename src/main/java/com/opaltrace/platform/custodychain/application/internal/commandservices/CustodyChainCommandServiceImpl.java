package com.opaltrace.platform.custodychain.application.internal.commandservices;

import com.opaltrace.platform.custodychain.application.commandservices.CustodyChainCommandService;
import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.domain.model.commands.AcceptCustodyCommand;
import com.opaltrace.platform.custodychain.domain.model.commands.UpdateLocationCommand;
import com.opaltrace.platform.custodychain.domain.model.entities.LocationUpdate;
import com.opaltrace.platform.custodychain.domain.model.events.DelayedTransportAlertEvent;
import com.opaltrace.platform.custodychain.domain.repositories.CustodyTransferRepository;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CustodyChainCommandServiceImpl implements CustodyChainCommandService {

    private final CustodyTransferRepository custodyTransferRepository;
    private final MineralBatchRepository mineralBatchRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustodyChainCommandServiceImpl(CustodyTransferRepository custodyTransferRepository,
                                          MineralBatchRepository mineralBatchRepository,
                                          ApplicationEventPublisher eventPublisher) {
        this.custodyTransferRepository = custodyTransferRepository;
        this.mineralBatchRepository = mineralBatchRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Long, ApplicationError> handle(AcceptCustodyCommand command) {
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
        if (batch.getStatus() != BatchStatus.EN_ORIGEN) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "invalid-status",
                    "Expected EN_ORIGEN, got: " + batch.getStatus()));
        }
        var transfer = new CustodyTransfer(command);
        var saved = custodyTransferRepository.save(transfer);
        batch.transitionStatus(BatchStatus.EN_TRANSITO);
        mineralBatchRepository.save(batch);
        return Result.success(saved.getId());
    }

    @Override
    public Result<Long, ApplicationError> handle(UpdateLocationCommand command) {
        var transferOpt = custodyTransferRepository.findByBatchPk(command.batchPk());
        if (transferOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("CustodyTransfer", command.batchPk().toString()));
        }
        var transfer = transferOpt.get();
        var update = new LocationUpdate(command.latitude(), command.longitude(), command.recordedByUserId());
        transfer.addLocationUpdate(update);

        long minutesSinceStart = Duration.between(transfer.getStartedAt(), LocalDateTime.now()).toMinutes();
        if (minutesSinceStart > command.maxRouteMinutes()) {
            eventPublisher.publishEvent(new DelayedTransportAlertEvent(
                    transfer.getBatchId(),
                    transfer.getBatchPk(),
                    transfer.getCustodyHolderUserId(),
                    update.getRecordedAt(),
                    minutesSinceStart
            ));
        }

        var saved = custodyTransferRepository.save(transfer);
        return Result.success(saved.getId());
    }
}
