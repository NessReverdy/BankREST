package com.example.bankcards.service.user.impl;

import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  @Override
  public UserResponse createUser(UserRequest userRequest) {
    return null;
  }

  @Override
  public UserResponse updateUser(UserRequest userRequest) {
    return null;
  }

  @Override
  public boolean deleteUserById(Long id) {
    return false;
  }

  @Override
  public UserResponse getUserById(Long id) {
    return null;
  }

  @Override
  public Page<UserResponse> getAllUsers(Pageable pageable) {
    return null;
  }
}
