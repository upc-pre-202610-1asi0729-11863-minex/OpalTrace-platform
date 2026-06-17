package com.opaltrace.platform.iam.interfaces.rest;

import com.opaltrace.platform.iam.application.commandservices.UserCommandService;
import com.opaltrace.platform.iam.application.queryservices.UserQueryService;
import com.opaltrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.opaltrace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.opaltrace.platform.iam.interfaces.rest.resources.*;
import com.opaltrace.platform.iam.interfaces.rest.transform.*;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User registration and management endpoints")
public class UsersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping("/register/enterprise")
    @Operation(summary = "Register enterprise user", description = "Registers a Mining or Jewelry company user (Gold plan by default)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enterprise user registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data (RUC, email format, password)"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<?> registerEnterpriseUser(@Valid @RequestBody RegisterEnterpriseUserResource resource) {
        var command = RegisterEnterpriseUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command)
                .flatMap(userId -> userQueryService.handle(new GetUserByIdQuery(userId))
                        .<Result<com.opaltrace.platform.iam.domain.model.aggregates.User, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("User", userId.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PostMapping("/register/consumer")
    @Operation(summary = "Register consumer user", description = "Registers a final consumer user (Silver plan)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consumer user registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<?> registerConsumerUser(@Valid @RequestBody RegisterConsumerUserResource resource) {
        var command = RegisterConsumerUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command)
                .flatMap(userId -> userQueryService.handle(new GetUserByIdQuery(userId))
                        .<Result<com.opaltrace.platform.iam.domain.model.aggregates.User, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("User", userId.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(
            @PathVariable
            @Parameter(description = "User unique identifier", example = "1", required = true)
            Long userId) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class)))
    })
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery())
                .stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(users);
    }
}
