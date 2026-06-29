package com.example.bankcards.service.block;

import com.example.bankcards.dto.block.BlockProcess;
import com.example.bankcards.dto.block.BlockRequest;
import com.example.bankcards.dto.block.BlockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockRequestService {
  BlockResponse createBlockRequest(Long userId, BlockRequest request);
  BlockResponse processBlockRequest(Long requestId, BlockProcess request);
  Page<BlockResponse> getAllRequests(Pageable pageable);
  Page<BlockResponse> getUserRequests(Long userId, Pageable pageable);
  BlockResponse getRequestById(Long requestId);
  void cancelRequest(Long requestId, Long userId);
}
