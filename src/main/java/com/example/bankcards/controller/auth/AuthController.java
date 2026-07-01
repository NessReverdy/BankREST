package com.example.bankcards.controller.auth;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.security.jwt.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and session management API")
public interface AuthController {

  @PostMapping("/register")
  @Operation(summary = "Register new user",
    description = "Creates a new user account and returns JWT tokens")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User successfully registered",
      content = @Content(schema = @Schema(implementation = AuthResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "409", description = "User already exists")
  })
  ResponseEntity<AuthResponse> register(
    @Parameter(description = "User registration details", required = true)
    @Valid
    @RequestBody
    UserRequest userRequest
  );

  @PostMapping("/login")
  @Operation(summary = "User login",
    description = "Authenticates user with email and password")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
      content = @Content(schema = @Schema(implementation = AuthResponse.class))),
    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
    @ApiResponse(responseCode = "403", description = "Account is locked or disabled")
  })
  ResponseEntity<AuthResponse> login(
    @Parameter(description = "Login credentials", required = true)
    @Valid
    @RequestBody
    AuthRequest authRequest
  );

  @PostMapping("/logout")
  @Operation(summary = "User logout",
    description = "Invalidates the provided refresh token")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully logged out"),
    @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  ResponseEntity<Void> logout(
    @Parameter(description = "Refresh token to invalidate", required = true)
    @Valid
    @RequestBody
    RefreshTokenRequest refreshTokenRequest
  );

  @PostMapping("/refresh")
  @Operation(summary = "Refresh access token",
    description = "Obtains a new access token using a valid refresh token")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Token successfully refreshed",
      content = @Content(schema = @Schema(implementation = AuthResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
    @ApiResponse(responseCode = "401", description = "Refresh token expired or invalid")
  })
  ResponseEntity<AuthResponse> refresh(
    @Parameter(description = "Refresh token to obtain new access token", required = true)
    @Valid
    @RequestBody
    RefreshTokenRequest refreshTokenRequest
  );
}
