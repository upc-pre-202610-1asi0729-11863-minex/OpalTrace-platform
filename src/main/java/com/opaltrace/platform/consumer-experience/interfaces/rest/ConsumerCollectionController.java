package com.opaltrace.platform.consumerexperience.interfaces.rest;

import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.consumerexperience.domain.repositories.ProductVerificationRepository;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.MyJewelryItemResource;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.VerificationHistoryItemResource;
import com.opaltrace.platform.iam.domain.repositories.UserRepository;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/consumer", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Consumer Collection", description = "Authenticated consumer endpoints")
public class ConsumerCollectionController {

    private final ProductVerificationRepository productVerificationRepository;
    private final JewelryProductRepository jewelryProductRepository;
    private final UserRepository userRepository;

    public ConsumerCollectionController(ProductVerificationRepository productVerificationRepository,
                                         JewelryProductRepository jewelryProductRepository,
                                         UserRepository userRepository) {
        this.productVerificationRepository = productVerificationRepository;
        this.jewelryProductRepository = jewelryProductRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-jewelry")
    @Operation(summary = "List my authentic jewelry", description = "Returns the certificates verified as authentic by the current user")
    public List<MyJewelryItemResource> getMyJewelry(@AuthenticationPrincipal Long userId) {
        return productVerificationRepository.findByConsumerIdAndVerificationResult(userId, VerificationResult.AUTHENTIC).stream()
                .map(verification -> {
                    var productOpt = verification.getProductId() != null
                            ? jewelryProductRepository.findById(verification.getProductId())
                            : java.util.Optional.<com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct>empty();

                    String productName = productOpt.map(p -> p.getName()).orElse(verification.getCertificateId());
                    String certificationState = productOpt.map(p -> p.getCertificationState().name()).orElse(null);
                    String jewelerName = productOpt
                            .flatMap(p -> userRepository.findById(p.getJewelryId()))
                            .map(jeweler -> jeweler.getCompanyName())
                            .orElse(null);

                    return new MyJewelryItemResource(
                            verification.getCertificateId(),
                            productName,
                            jewelerName,
                            certificationState,
                            verification.getVerifiedAt().toString()
                    );
                })
                .toList();
    }

    @GetMapping("/history")
    @Operation(summary = "Verification history", description = "Returns all verification attempts made by the current user")
    public List<VerificationHistoryItemResource> getHistory(@AuthenticationPrincipal Long userId) {
        return productVerificationRepository.findByConsumerId(userId).stream()
                .map(verification -> {
                    String productName = jewelryProductRepository.findByCertificateNumber(verification.getCertificateId())
                            .map(p -> p.getName())
                            .orElse(verification.getCertificateId());

                    return new VerificationHistoryItemResource(
                            verification.getCertificateId(),
                            productName,
                            verification.getVerifiedAt().toString(),
                            verification.getVerificationResult().name()
                    );
                })
                .toList();
    }
}
