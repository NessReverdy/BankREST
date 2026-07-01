package com.example.bankcards.controller.card;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardSearchFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/card")
@Tag(name = "Card Management", description = "APIs for managing bank cards")
public interface CardController {

  @PostMapping("/create")
  @Operation(
    summary = "Create a new card",
    description = "Creates a new bank card for a user (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Card created successfully",
      content = @Content(schema = @Schema(implementation = CardResponse.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid card data"),
    @ApiResponse(responseCode = "409", description = "Card already exists")
  })
  ResponseEntity<CardResponse> createCard(
    @Parameter(description = "Card creation details", required = true)
    @Valid
    @RequestBody
    CardRequest request
  );

  @PatchMapping("/update/{id}")
  @Operation(
    summary = "Update card details",
    description = "Updates an existing card's information (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Card updated successfully",
      content = @Content(schema = @Schema(implementation = CardResponse.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid update data"),
    @ApiResponse(responseCode = "404", description = "Card not found")
  })
  ResponseEntity<CardResponse> updateCard(
    @Parameter(description = "Card ID to update", required = true)
    @PathVariable Long id,
    @Parameter(description = "Updated card details", required = true)
    @Valid
    @RequestBody
    CardRequest request
  );

  @DeleteMapping("/delete/{id}")
  @Operation(
    summary = "Delete a card",
    description = "Deletes a card by its ID (only for Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Card deleted successfully",
      content = @Content(schema = @Schema(implementation = Boolean.class))
    ),
    @ApiResponse(responseCode = "404", description = "Card not found"),
    @ApiResponse(responseCode = "409", description = "Cannot delete card with active operations")
  })
  ResponseEntity<Void> deleteCardById(
    @Parameter(description = "Card ID to delete", required = true)
    @PathVariable Long id
  );

  @GetMapping("/{id}")
  @Operation(
    summary = "Get card by ID",
    description = "Retrieves detailed card information by its ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Card found",
      content = @Content(schema = @Schema(implementation = CardResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "Card not found")
  })
  ResponseEntity<CardResponse> getCardById(
    @Parameter(description = "Card ID", required = true)
    @PathVariable Long id
  );

  @GetMapping("/all")
  @Operation(
    summary = "Get all cards",
    description = "Retrieves a paginated list of all cards"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Cards retrieved successfully",
      content = @Content(schema = @Schema(implementation = Page.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  ResponseEntity<Page<CardResponse>> getAllCards(
    @Parameter(description = "Pagination and sorting parameters")
    @PageableDefault(
      size = 5,
      sort = "id",
      direction = Sort.Direction.DESC
    )
    Pageable pageable
  );

  @GetMapping("/balance/{id}")
  @Operation(
    summary = "Get card balance",
    description = "Retrieves the current balance of a specific card"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Balance retrieved successfully",
      content = @Content(schema = @Schema(implementation = CardResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "Card not found")
  })
  ResponseEntity<CardBalanceResponse> getCardBalanceById(
    @Parameter(description = "Card ID", required = true)
    @PathVariable Long id
  );

  @GetMapping("/search")
  @Operation(
    summary = "Search cards",
    description = "Searches for cards based on filter criteria with pagination"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Search completed successfully",
      content = @Content(schema = @Schema(implementation = Page.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid search filter"),
    @ApiResponse(responseCode = "404", description = "No cards found matching criteria")
  })
  ResponseEntity<Page<CardResponse>> searchCards(
    @Parameter(description = "Search filter criteria", required = true)
    @Valid
    @RequestBody
    CardSearchFilter cardSearchFilter,
    @Parameter(description = "Pagination and sorting parameters")
    @PageableDefault(
      size = 5,
      direction = Sort.Direction.DESC
    )
    Pageable pageable
  );
}
