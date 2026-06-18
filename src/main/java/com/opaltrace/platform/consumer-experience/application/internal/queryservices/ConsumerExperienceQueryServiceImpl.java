package com.opaltrace.platform.consumerexperience.application.internal.queryservices;

import com.opaltrace.platform.consumerexperience.application.queryservices.ConsumerExperienceQueryService;
import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.entities.TraceabilityPoint;
import com.opaltrace.platform.consumerexperience.domain.model.queries.GetTraceabilityMapByCertificateIdQuery;
import com.opaltrace.platform.consumerexperience.domain.model.queries.GetVerificationByCertificateIdQuery;
import com.opaltrace.platform.consumerexperience.domain.repositories.ProductVerificationRepository;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerExperienceQueryServiceImpl implements ConsumerExperienceQueryService {

    private final ProductVerificationRepository productVerificationRepository;
    private final JewelryProductRepository jewelryProductRepository;
    private final MineralBatchRepository mineralBatchRepository;

    public ConsumerExperienceQueryServiceImpl(ProductVerificationRepository productVerificationRepository,
                                               JewelryProductRepository jewelryProductRepository,
                                               MineralBatchRepository mineralBatchRepository) {
        this.productVerificationRepository = productVerificationRepository;
        this.jewelryProductRepository = jewelryProductRepository;
        this.mineralBatchRepository = mineralBatchRepository;
    }

    @Override
    public Optional<ProductVerification> handle(GetVerificationByCertificateIdQuery query) {
        return productVerificationRepository.findByCertificateId(query.certificateId());
    }

    @Override
    public List<TraceabilityPoint> handle(GetTraceabilityMapByCertificateIdQuery query) {
        var productOpt = jewelryProductRepository.findByCertificateNumber(query.certificateId());
        if (productOpt.isEmpty()) return List.of();

        var product = productOpt.get();
        if (product.getBatchPk() == null) return List.of();

        var batchOpt = mineralBatchRepository.findById(product.getBatchPk());
        if (batchOpt.isEmpty()) return List.of();

        var batch = batchOpt.get();
        var coords = batch.getOriginCoordinates();
        var createdAt = LocalDateTime.now();
        var hash = batch.getBlockchainTxHash() != null ? batch.getBlockchainTxHash() : "0x000000";
        var status = batch.getStatus();

        List<TraceabilityPoint> points = new ArrayList<>();

        points.add(new TraceabilityPoint(
                "MINERAL_EXTRACTED",
                coords.latitude(),
                coords.longitude(),
                createdAt,
                "Mining Company",
                hash,
                1
        ));

        if (isAtLeast(status, BatchStatus.EN_TRANSITO)) {
            points.add(new TraceabilityPoint(
                    "TRANSPORT_STARTED",
                    coords.latitude(),
                    coords.longitude(),
                    createdAt.plusHours(1),
                    "Supervisor",
                    simulateHash(),
                    2
            ));
        }

        if (isAtLeast(status, BatchStatus.EN_PLANTA)) {
            points.add(new TraceabilityPoint(
                    "BATCH_RECEIVED_AT_REFINERY",
                    coords.latitude(),
                    coords.longitude(),
                    createdAt.plusHours(2),
                    "Refinery",
                    simulateHash(),
                    3
            ));
        }

        if (status == BatchStatus.CERTIFICADO) {
            points.add(new TraceabilityPoint(
                    "MATERIAL_RECEIVED_AT_JEWELRY",
                    coords.latitude(),
                    coords.longitude(),
                    createdAt.plusHours(3),
                    "Jewelry",
                    simulateHash(),
                    4
            ));
        }

        return points;
    }

    private boolean isAtLeast(BatchStatus current, BatchStatus target) {
        return current.ordinal() >= target.ordinal();
    }

    private String simulateHash() {
        return "0x" + Long.toHexString(System.currentTimeMillis()) + Long.toHexString((long) (Math.random() * Long.MAX_VALUE));
    }
}
