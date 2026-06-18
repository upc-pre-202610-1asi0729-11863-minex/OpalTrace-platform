package com.opaltrace.platform.iam.interfaces.rest;

import com.opaltrace.platform.iam.application.commandservices.UserCommandService;
import com.opaltrace.platform.iam.infrastructure.tokens.jwt.services.TokenService;
import com.opaltrace.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.opaltrace.platform.iam.interfaces.rest.resources.SignInResource;
import com.opaltrace.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.opaltrace.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication endpoints for sign in")
public class AuthenticationController {

    private final UserCommandService userCommandService;
    private final TokenService tokenService;

    public AuthenticationController(UserCommandService userCommandService, TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = AuthenticatedUserResource.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or account locked"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command).map(user -> {
            var token = tokenService.generateToken(user);
            return UserResourceFromEntityAssembler.toAuthenticatedResourceFromEntity(user, token);
        });
        return ResponseEntityAssembler.toResponseEntityFromResult(result, r -> r, HttpStatus.OK);
    }
}
