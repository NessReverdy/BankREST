package com.example.bankcards.controller.user.impl;

import com.example.bankcards.controller.user.UserController;
import com.example.bankcards.dto.user.RoleRequest;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
  private final UserService userService;

  @Override
  public ResponseEntity<UserResponse> updateUser(Long id, UserRequest userRequest) {
    return ResponseEntity.ok(userService.updateUser(id, userRequest));
  }

  @Override
  public ResponseEntity<Void> deleteUserById(Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserResponse> getUserById(Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Override
  public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
    return ResponseEntity.ok(userService.getAllUsers(pageable));
  }

  @Override
  public ResponseEntity<UserResponse> changeRoleById(Long id, RoleRequest roleRequest) {
    return ResponseEntity.ok(userService.changeRoleById(id, roleRequest));
  }
}
