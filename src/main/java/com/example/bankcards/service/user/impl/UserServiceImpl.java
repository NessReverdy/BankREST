package com.example.bankcards.service.user.impl;

import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.impl.UserAlreadyExistsException;
import com.example.bankcards.exception.impl.UserNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.status.UserRole;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

  private final UserRepository userRepo;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserResponse createUser(UserRequest userRequest) {
    if (userRepo.existsByUsername(userRequest.username())){
      throw new UserAlreadyExistsException("User with username: " + userRequest.username() + " already exists");
    }

    String encodedPassword = passwordEncoder.encode(userRequest.password());
    UserEntity user = new UserEntity();

    user.setFirstName(userRequest.firstName());
    user.setMiddleName(userRequest.middleName());
    user.setLastName(userRequest.lastName());
    user.setUsername(userRequest.username());
    user.setPassword(encodedPassword);
    user.setRole(UserRole.USER);

    userRepo.save(user);

    log.info("Creating user with id: {}", user.getId());
    return mapper.toResponse(user);
  }

  @Override
  public UserResponse updateUser(Long id,UserRequest userRequest) {
    UserEntity user = findUserById(id);

    String encodedPassword = passwordEncoder.encode(userRequest.password());

    user.setFirstName(userRequest.firstName());
    user.setMiddleName(userRequest.middleName());
    user.setLastName(userRequest.lastName());
    user.setUsername(userRequest.username());
    user.setPassword(encodedPassword);

    userRepo.save(user);

    log.info("Updating user with id: {}", id);
    return mapper.toResponse(user);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public boolean deleteUserById(Long id) {
    try{
      userRepo.deleteById(id);
      log.info("Deleted user with id: " + id);
      return true;
    } catch (UserNotFoundException e){
      throw new UserNotFoundException("User with id: " + id + " not found");
    }
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserById(Long id) {
    return mapper.toResponse(findUserById(id));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public Page<UserResponse> getAllUsers(Pageable pageable) {
    Page<UserEntity> users = userRepo.findAll(pageable);
    return users.map(mapper::toResponse);
  }

  @Override
  public UserResponse changeRoleById(Long id, UserRole role) {
    UserEntity user = findUserById(id);

    user.setRole(role);
    userRepo.save(user);

    log.info("Changed user with id: {}", id);
    return mapper.toResponse(user);
  }

  private UserEntity findUserById(Long id) {
    return userRepo.findById(id).orElseThrow(
      () -> new UserNotFoundException("User not found.")
    );
  }
}
