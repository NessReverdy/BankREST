package com.example.bankcards.controller.user;

import com.example.bankcards.dto.user.RoleRequest;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/user")
@Tag(name = "User Management", description = "APIs for managing user accounts and profiles")
@SecurityRequirement(name = "bearerAuth")
public interface UserController {

  @PatchMapping("/update/{id}")
  @Operation(
    summary = "Update user information",
    description = "Updates an existing user's profile information. Only admins can update other users. Users can update their own profile."
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User updated successfully",
      content = @Content(schema = @Schema(implementation = UserResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
  })
  ResponseEntity<UserResponse> updateUser(
    @Parameter(description = "User ID to update", required = true, example = "1")
    @PathVariable Long id,
    @Parameter(description = "Updated user details", required = true)
    @Valid
    @RequestBody
    UserRequest userRequest
  );

  @DeleteMapping("/delete/{id}")
  @Operation(
    summary = "Delete user",
    description = "Deletes a user by ID. Only admins can delete users (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User deleted successfully",
      content = @Content(schema = @Schema(implementation = UserResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
  })
  ResponseEntity<Void> deleteUserById(
    @Parameter(description = "User ID to delete", required = true, example = "1")
    @PathVariable Long id
  );

  @GetMapping("/{id}")
  @Operation(
    summary = "Get user by ID",
    description =
      "Retrieves detailed user information by ID. " +
        "Admins can view any user. Users can only view their own profile (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(schema = @Schema(implementation = UserResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<UserResponse> getUserById(
    @Parameter(description = "User ID", required = true, example = "1")
    @PathVariable Long id
  );

  @GetMapping("/all")
  @Operation(
    summary = "Get all users",
    description = "Retrieves a paginated list of all users. Only admins can access this endpoint (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Users retrieved successfully",
      content = @Content(schema = @Schema(implementation = Page.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
  })
  ResponseEntity<Page<UserResponse>> getAllUsers(
    @Parameter(description = "Pagination and sorting parameters")
    @PageableDefault(
      size = 5,
      sort = "id",
      direction = Sort.Direction.DESC
    )
    Pageable pageable
  );

  @PatchMapping("/role/{id}")
  @Operation(
    summary = "Change user role",
    description = "Changes the role of an existing user. Only admins can change user roles (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Role updated successfully",
      content = @Content(schema = @Schema(implementation = UserResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "User not found"),
  })
  ResponseEntity<UserResponse> changeRoleById(
    @Parameter(description = "User ID to change role", required = true, example = "1")
    @PathVariable Long id,
    @Parameter(description = "New role details", required = true)
    @Valid
    @RequestBody
    RoleRequest roleRequest
  );
}