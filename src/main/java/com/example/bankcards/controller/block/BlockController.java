package com.example.bankcards.controller.block;

import com.example.bankcards.dto.block.BlockProcess;
import com.example.bankcards.dto.block.BlockRequest;
import com.example.bankcards.dto.block.BlockResponse;
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
@RequestMapping("/block")
@Tag(name = "Block Request Management", description = "APIs for managing card block requests")
public interface BlockController {

  @PostMapping("/create")
  @Operation(
    summary = "Create block request",
    description = "Creates a new card blocking request"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Block request created successfully",
      content = @Content(schema = @Schema(implementation = BlockResponse.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "409", description = "Block request already exists")
  })
  ResponseEntity<BlockResponse> createBlockRequest(
    @Parameter(description = "Block request details", required = true)
    @Valid
    @RequestBody
    BlockRequest blockRequest
  );

  @PostMapping("/process/{id}")
  @Operation(
    summary = "Process block request",
    description = "Processes an existing block request (approve or reject (only Admins))"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Block request processed successfully",
      content = @Content(schema = @Schema(implementation = BlockResponse.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid processing data"),
    @ApiResponse(responseCode = "404", description = "Block request not found"),
    @ApiResponse(responseCode = "409", description = "Block request already processed")
  })
  ResponseEntity<BlockResponse> processBlockRequest(
    @Parameter(description = "ID of the block request to process", required = true)
    @PathVariable Long id,
    @Parameter(description = "Processing decision details", required = true)
    @Valid
    @RequestBody
    BlockProcess blockProcess
  );

  @GetMapping("/all")
  @Operation(
    summary = "Get all block requests",
    description = "Retrieves a paginated list of all block requests (only Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Block requests retrieved successfully",
      content = @Content(schema = @Schema(implementation = Page.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  ResponseEntity<Page<BlockResponse>> getAllRequests(
    @Parameter(description = "Pagination and sorting parameters")
    @PageableDefault(
      size = 5,
      sort = "createdAt",
      direction = Sort.Direction.DESC
    )
    Pageable pageable
  );

  @GetMapping("/user/{id}")
  @Operation(
    summary = "Get user block requests",
    description = "Retrieves a paginated list of block requests for a specific user (only Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "User block requests retrieved successfully",
      content = @Content(schema = @Schema(implementation = Page.class))
    ),
    @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<Page<BlockResponse>> getUserRequests(
    @Parameter(description = "User ID", required = true)
    @PathVariable Long id,
    @Parameter(description = "Pagination and sorting parameters")
    @PageableDefault(
      size = 5,
      sort = "createdAt",
      direction = Sort.Direction.DESC
    )
    Pageable pageable
  );

  @GetMapping("/{id}")
  @Operation(
    summary = "Get block request by ID",
    description = "Retrieves detailed information about a specific block request (only Admins)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Block request found",
      content = @Content(schema = @Schema(implementation = BlockResponse.class))
    ),
    @ApiResponse(responseCode = "404", description = "Block request not found")
  })
  ResponseEntity<BlockResponse> getRequestById(
    @Parameter(description = "Block request ID", required = true)
    @PathVariable Long id
  );

  @PostMapping("/cancel/{id}")
  @Operation(
    summary = "Cancel block request",
    description = "Cancels an existing block request that hasn't been processed yet"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Block request cancelled successfully"),
    @ApiResponse(responseCode = "404", description = "Block request not found"),
    @ApiResponse(responseCode = "409", description = "Block request cannot be cancelled (already processed)")
  })
  ResponseEntity<Void> cancelRequest(
    @Parameter(description = "ID of the block request to cancel", required = true)
    @PathVariable Long id
  );
}
