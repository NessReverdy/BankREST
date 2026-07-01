package com.example.bankcards.controller.block.impl;

import com.example.bankcards.controller.block.BlockController;
import com.example.bankcards.dto.block.BlockProcess;
import com.example.bankcards.dto.block.BlockRequest;
import com.example.bankcards.dto.block.BlockResponse;
import com.example.bankcards.service.block.BlockRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlockControllerImpl implements BlockController {
  private final BlockRequestService blockRequestService;

  @Override
  public ResponseEntity<BlockResponse> createBlockRequest(BlockRequest blockRequest) {
    return ResponseEntity.ok(blockRequestService.createBlockRequest(blockRequest));
  }

  @Override
  public ResponseEntity<BlockResponse> processBlockRequest(Long id, BlockProcess blockProcess) {
    return ResponseEntity.ok(blockRequestService.processBlockRequest(id, blockProcess));
  }

  @Override
  public ResponseEntity<Page<BlockResponse>> getAllRequests(Pageable pageable) {
    return ResponseEntity.ok(blockRequestService.getAllRequests(pageable));
  }

  @Override
  public ResponseEntity<Page<BlockResponse>> getUserRequests(Long id, Pageable pageable) {
    return ResponseEntity.ok(blockRequestService.getUserRequests(id, pageable));
  }

  @Override
  public ResponseEntity<BlockResponse> getRequestById(Long id) {
    return ResponseEntity.ok(blockRequestService.getRequestById(id));
  }

  @Override
  public ResponseEntity<Void> cancelRequest(Long id) {
    blockRequestService.cancelRequest(id);
    return ResponseEntity.noContent().build();
  }
}
