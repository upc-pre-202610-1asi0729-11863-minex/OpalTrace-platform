package com.opaltrace.platform.consumerexperience.application.internal.commandservices;

import com.opaltrace.platform.consumerexperience.application.commandservices.ConsumerExperienceCommandService;
import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.commands.RecordTraceabilityViewCommand;
import com.opaltrace.platform.consumerexperience.domain.model.commands.VerifyProductAuthenticityCommand;
import com.opaltrace.platform.consumerexperience.domain.model.events.SuspiciousVerificationAttemptEvent;
import com.opaltrace.platform.consumerexperience.domain.model.events.TraceabilityViewedEvent;
import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.consumerexperience.domain.repositories.ProductVerificationRepository;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.CertificationState;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumerExperienceCommandServiceImpl implements ConsumerExperienceCommandService {

    private final ProductVerificationRepository productVerificationRepository;
    private final JewelryProductRepository jewelryProductRepository;
    private final MineralBatchRepository mineralBatchRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ConsumerExperienceCommandServiceImpl(ProductVerificationRepository productVerificationRepository,
                                                 JewelryProductRepository jewelryProductRepository,
                                                 MineralBatchRepository mineralBatchRepository,
                                                 ApplicationEventPublisher eventPublisher) {
        this.productVerificationRepository = productVerificationRepository;
        this.jewelryProductRepository = jewelryProductRepository;
        this.mineralBatchRepository = mineralBatchRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ProductVerification, ApplicationError> handle(VerifyProductAuthenticityCommand cmd) {
        var productOpt = jewelryProductRepository.findByCertificateNumber(cmd.certificateId());

        if (productOpt.isEmpty()) {
            var verification = new ProductVerification(cmd, VerificationResult.NOT_VERIFIABLE,
                    "QR not registered in OpalTrace", null, null);
            var saved = productVerificationRepository.save(verification);
            return Result.success(saved);
        }

        var product = productOpt.get();

        if (product.getCertificationState() != CertificationState.CERTIFIED) {
            var verification = new ProductVerification(cmd, VerificationResult.NOT_VERIFIABLE,
                    "Certificate state: " + product.getCertificationState(), product.getId(), product.getBatchId());
            var saved = productVerificationRepository.save(verification);
            return Result.success(saved);
        }

        if (product.getBatchPk() != null) {
            var batchOpt = mineralBatchRepository.findById(product.getBatchPk());
            if (batchOpt.isPresent() && batchOpt.get().isBlocked()) {
                var verification = new ProductVerification(cmd, VerificationResult.NOT_VERIFIABLE,
                        "Anomaly detected in batch " + product.getBatchId(), product.getId(), product.getBatchId());
                var saved = productVerificationRepository.save(verification);
                return Result.success(saved);
            }
        }

        var since = LocalDateTime.now().minusMinutes(5);
        long recentCount = productVerificationRepository.countRecentVerifications(cmd.certificateId(), since);
        if (recentCount > 2) {
            eventPublisher.publishEvent(new SuspiciousVerificationAttemptEvent(
                    cmd.certificateId(), cmd.verifierIp(), LocalDateTime.now()));
        }

        var verification = new ProductVerification(cmd, VerificationResult.AUTHENTIC,
                null, product.getId(), product.getBatchId());
        var saved = productVerificationRepository.save(verification);
        return Result.success(saved);
    }

    @Override
    public Result<Long, ApplicationError> handle(RecordTraceabilityViewCommand cmd) {
        eventPublisher.publishEvent(new TraceabilityViewedEvent(cmd.certificateId(), cmd.viewerIp(), LocalDateTime.now()));
        var verifyCmd = new VerifyProductAuthenticityCommand(cmd.certificateId(), cmd.viewerIp());
        var minimal = new ProductVerification(verifyCmd, VerificationResult.AUTHENTIC, null, null, null);
        var saved = productVerificationRepository.save(minimal);
        return Result.success(saved.getId());
    }
}
