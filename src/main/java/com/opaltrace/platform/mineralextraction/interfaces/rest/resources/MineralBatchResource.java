package com.opaltrace.platform.mineralextraction.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;

import java.util.List;

public record MineralBatchResource(
        Long id,
        String batchId,
        MineralType mineralType,
        double weightKg,
        double originLatitude,
        double originLongitude,
        BatchStatus status,
        boolean blocked,
        Long supervisorId,
        Long miningCompanyId,
        String blockchainTxHash,
        Long parentBatchId,
        String qrCodeData,
        List<AnomalyReportResource> anomalyReports
) {}
