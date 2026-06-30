package com.example.bankcards.service.user;

import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.status.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserResponse createUser(UserRequest userRequest);
  UserResponse updateUser(Long id, UserRequest userRequest);
  boolean deleteUserById(Long id);
  UserResponse getUserById(Long id);
  Page<UserResponse> getAllUsers(Pageable pageable);
  UserResponse changeRoleById(Long id, UserRole role);
}
