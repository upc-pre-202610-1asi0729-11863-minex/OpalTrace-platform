package com.opaltrace.platform.jewelryinventory.application.internal.commandservices;

import com.opaltrace.platform.jewelryinventory.application.commandservices.JewelryInventoryCommandService;
import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.CertifyProductCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.GenerateCertificateCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.ReceiveCertifiedMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.RegisterExternalMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.CertificationState;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class JewelryInventoryCommandServiceImpl implements JewelryInventoryCommandService {

    private final JewelryProductRepository jewelryProductRepository;
    private final MineralBatchRepository mineralBatchRepository;

    public JewelryInventoryCommandServiceImpl(JewelryProductRepository jewelryProductRepository,
                                               MineralBatchRepository mineralBatchRepository) {
        this.jewelryProductRepository = jewelryProductRepository;
        this.mineralBatchRepository = mineralBatchRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(ReceiveCertifiedMaterialCommand command) {
        var batchOpt = mineralBatchRepository.findById(command.batchPk());
        if (batchOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("MineralBatch", command.batchPk().toString()));
        }
        var batch = batchOpt.get();
        if (batch.isBlocked()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "batch-blocked",
                    "Batch %s is blocked by active anomaly".formatted(command.batchId())));
        }
        var status = batch.getStatus();
        if (status != BatchStatus.EN_PLANTA && status != BatchStatus.PROCESADO && status != BatchStatus.CERTIFICADO) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "incomplete-traceability",
                    "Batch status %s does not meet requirements".formatted(status)));
        }
        var product = new JewelryProduct(command);
        var saved = jewelryProductRepository.save(product);
        return Result.success(saved.getId());
    }

    @Override
    public Result<Long, ApplicationError> handle(RegisterExternalMaterialCommand command) {
        var product = new JewelryProduct(command);
        var saved = jewelryProductRepository.save(product);
        return Result.success(saved.getId());
    }

    @Override
    public Result<Long, ApplicationError> handle(CertifyProductCommand command) {
        var productOpt = jewelryProductRepository.findById(command.productId());
        if (productOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("JewelryProduct", command.productId().toString()));
        }
        var product = productOpt.get();
        if (!product.isCertifiedSource()) {
            product.rejectCertification("Product contains external material - Not eligible for OpalTrace certification");
            jewelryProductRepository.save(product);
            return Result.failure(ApplicationError.businessRuleViolation(
                    "non-certified-source",
                    "Product contains external material - Not eligible for OpalTrace certification"));
        }
        if (product.getBatchPk() != null) {
            var batchOpt = mineralBatchRepository.findById(product.getBatchPk());
            if (batchOpt.isPresent() && batchOpt.get().isBlocked()) {
                return Result.failure(ApplicationError.businessRuleViolation(
                        "batch-blocked",
                        "Batch %s is blocked by active anomaly".formatted(product.getBatchId())));
            }
        }
        int year = Year.now().getValue();
        long count = jewelryProductRepository.countByYear(year);
        String certificateNumber = "CERT-%d-%04d".formatted(year, count + 1);
        product.certify(certificateNumber);
        var saved = jewelryProductRepository.save(product);
        return Result.success(saved.getId());
    }

    @Override
    public Result<Long, ApplicationError> handle(GenerateCertificateCommand command) {
        var productOpt = jewelryProductRepository.findById(command.productId());
        if (productOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("JewelryProduct", command.productId().toString()));
        }
        var product = productOpt.get();
        if (product.getCertificationState() != CertificationState.CERTIFIED) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "not-certified",
                    "Product is not in CERTIFIED state, current state: " + product.getCertificationState()));
        }
        product.recordCertificateDownload();
        var saved = jewelryProductRepository.save(product);
        return Result.success(saved.getId());
    }
}
