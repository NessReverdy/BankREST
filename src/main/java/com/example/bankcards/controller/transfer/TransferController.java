package com.example.bankcards.controller.transfer;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
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

import java.util.List;

@Validated
@RequestMapping("/transfer")
@Tag(name = "Transfer Management", description = "APIs for managing money transfers between cards")
public interface TransferController {

  @PostMapping
  @Operation(
    summary = "Transfer money between cards",
    description = "Performs a money transfer from one card to another. Supports multiple transfers in a single request."
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Transfer completed successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = CardBalanceResponse.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Source or destination card not found"
    ),
    @ApiResponse(
      responseCode = "409",
      description = "Transfer conflict (card blocked, expired, etc.)"
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Internal server error during transfer"
    )
  })
  ResponseEntity<List<CardBalanceResponse>> transfer(
    @Parameter(
      description = "Transfer request containing source card, destination card, and amount",
      required = true
    )
    @Valid
    @RequestBody
    TransferRequest transferRequest
  );
}
