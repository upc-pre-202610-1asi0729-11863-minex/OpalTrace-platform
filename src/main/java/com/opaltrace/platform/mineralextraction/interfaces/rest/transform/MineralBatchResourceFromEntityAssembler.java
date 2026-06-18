package com.opaltrace.platform.mineralextraction.interfaces.rest.transform;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.AnomalyReportResource;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.MineralBatchResource;

public final class MineralBatchResourceFromEntityAssembler {

    private MineralBatchResourceFromEntityAssembler() {}

    public static MineralBatchResource toResourceFromEntity(MineralBatch batch) {
        var anomalies = batch.getAnomalyReports().stream()
                .map(MineralBatchResourceFromEntityAssembler::toAnomalyResource)
                .toList();
        return new MineralBatchResource(
                batch.getId(),
                batch.getBatchId(),
                batch.getMineralType(),
                batch.getWeight().value(),
                batch.getOriginCoordinates().latitude(),
                batch.getOriginCoordinates().longitude(),
                batch.getStatus(),
                batch.isBlocked(),
                batch.getSupervisorId(),
                batch.getMiningCompanyId(),
                batch.getBlockchainTxHash(),
                batch.getParentBatchId(),
                batch.getQrCodeData(),
                anomalies
        );
    }

    public static AnomalyReportResource toAnomalyResource(AnomalyReport report) {
        return new AnomalyReportResource(
                report.getId(),
                report.getDescription(),
                report.getCategory(),
                report.getPhotoEvidenceUrl(),
                report.isResolved(),
                report.getReportedAt(),
                report.getReportedByUserId()
        );
    }
}
