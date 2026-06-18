package com.opaltrace.platform.refineryprocessing.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;
import jakarta.validation.constraints.Positive;

public record SubLotRequestResource(
        @Positive double weightKg,
        MineralType mineralType
) {}
