package com.opaltrace.platform.mineralextraction.application.internal.commandservices;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.iam.domain.repositories.UserRepository;
import com.opaltrace.platform.mineralextraction.application.commandservices.MineralBatchCommandService;
import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.commands.*;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.GpsCoordinate;
import com.opaltrace.platform.mineralextraction.domain.repositories.AuthorizedZoneRepository;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class MineralBatchCommandServiceImpl implements MineralBatchCommandService {

    private final MineralBatchRepository mineralBatchRepository;
    private final AuthorizedZoneRepository authorizedZoneRepository;
    private final UserRepository userRepository;

    public MineralBatchCommandServiceImpl(MineralBatchRepository mineralBatchRepository,
                                          AuthorizedZoneRepository authorizedZoneRepository,
                                          UserRepository userRepository) {
        this.mineralBatchRepository = mineralBatchRepository;
        this.authorizedZoneRepository = authorizedZoneRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(RegisterMineralBatchCommand command) {
        if (command.supervisorId() != null) {
            var userOpt = userRepository.findById(command.supervisorId());
            if (userOpt.isPresent()) {
                PlanTier tier = userOpt.get().getPlanTier();
                if (tier == PlanTier.SILVER) {
                    return Result.failure(ApplicationError.planTierInsufficient("GOLD"));
                }
            }
        }

        var coordinate = new GpsCoordinate(command.latitude(), command.longitude());
        var authorizedZones = authorizedZoneRepository.findAllActive();
        boolean inAuthorizedZone = authorizedZones.stream().anyMatch(z -> z.containsCoordinate(coordinate));
        if (!inAuthorizedZone)
            return Result.failure(ApplicationError.businessRuleViolation(
                    "authorized-zone",
                    "Origin outside authorized zone — Coordinates: %s".formatted(coordinate)));

        try {
            var weight = new com.opaltrace.platform.mineralextraction.domain.model.valueobjects.WeightKg(command.weightKg());
            weight.validateMaxFor(command.mineralType());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("weight", e.getMessage()));
        }

        int sequence = mineralBatchRepository.countByYear(Year.now().getValue()) + 1;
        String batchId = MineralBatch.generateBatchId(sequence);

        try {
            var batch = new MineralBatch(command, batchId);
            batch = mineralBatchRepository.save(batch);
            return Result.success(batch.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("register-mineral-batch", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("register-mineral-batch", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(ReportAnomalyCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.batchPk());
        if (batchOpt.isEmpty())
            return Result.failure(ApplicationError.notFound("MineralBatch", command.batchPk().toString()));

        var batch = batchOpt.get();
        if (batch.isBlocked())
            return Result.failure(ApplicationError.businessRuleViolation(
                    "report-anomaly",
                    "Batch %s is already blocked by an active anomaly".formatted(batch.getBatchId())));

        try {
            var anomaly = new AnomalyReport(command.description(), command.category(),
                    command.photoEvidenceUrl(), command.reportedByUserId());
            batch.reportAnomaly(anomaly);
            mineralBatchRepository.save(batch);
            return Result.success(batch.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("report-anomaly", e.getMessage()));
        }
    }

    @Override
    public Result<MineralBatch, ApplicationError> handle(GenerateBatchQrCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.batchPk());
        if (batchOpt.isEmpty())
            return Result.failure(ApplicationError.notFound("MineralBatch", command.batchPk().toString()));

        var batch = batchOpt.get();
        try {
            batch.generateQr();
            var saved = mineralBatchRepository.save(batch);
            return Result.success(saved);
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("generate-qr", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("generate-qr", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(SyncOfflineBatchCommand command) {
        if (mineralBatchRepository.existsByBatchId(command.offlineBatchId()))
            return Result.success(-1L);

        var syncCommand = new RegisterMineralBatchCommand(
                command.mineralType(),
                command.weightKg(),
                command.latitude(),
                command.longitude(),
                command.supervisorId(),
                command.miningCompanyId()
        );
        return handle(syncCommand);
    }
}
