package com.opaltrace.platform.consumerexperience.interfaces.rest.resources;

public record MyJewelryItemResource(
        String certId,
        String productName,
        String jewelerName,
        String certificationState,
        String verifiedAt
) {
}
