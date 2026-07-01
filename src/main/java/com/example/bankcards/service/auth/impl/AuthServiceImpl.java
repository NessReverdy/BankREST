package com.example.bankcards.service.auth.impl;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.impl.TokenDataException;
import com.example.bankcards.exception.impl.UserAlreadyExistsException;
import com.example.bankcards.exception.impl.UserNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.dto.RefreshTokenRequest;
import com.example.bankcards.security.jwt.service.jwt.JwtService;
import com.example.bankcards.security.jwt.service.refresh.RefreshTokenService;
import com.example.bankcards.service.auth.AuthService;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);

  private final UserRepository userRepo;
  private final UserService userService;
  private final JwtService jwtService;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenService refreshTokenService;
  private final UserDetailsService userDetailsService;

  @Override
  public AuthResponse register(UserRequest userRequest) {
    if (userRepo.existsByUsername(userRequest.username())) {
      throw new UserAlreadyExistsException("User with username: " + userRequest.username() + " already exists");
    }

    UserResponse newUser = userService.createUser(userRequest);
    String accessToken = jwtService.generateAccessToken(mapper.toEntity(newUser));
    String refreshToken = jwtService.generateRefreshToken(mapper.toEntity(newUser));

    return new AuthResponse(accessToken, refreshToken);
  }

  @Override
  public AuthResponse login(AuthRequest authRequest) {
    UserEntity user = userRepo.findByUsername(authRequest.username())
      .orElseThrow(
        () -> new UserNotFoundException("User with username: " + authRequest.username() + " not found")
      );

    if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
      log.warn("Wrong password for user: " + authRequest.username());
      throw new UserNotFoundException("Wrong password for user: " + authRequest.username());
    }

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    return new AuthResponse(accessToken, refreshToken);
  }

  @Override
  public void logout(RefreshTokenRequest refreshTokenRequest) {
    String token = refreshTokenRequest.refreshToken();
    refreshTokenService.delete(token);
    log.info("Logged out user");
  }

  @Override
  public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
    String oldToken = refreshTokenRequest.refreshToken();
    String username = jwtService.extractUsername(oldToken);

    UserEntity user = userRepo.findByUsername(username)
      .orElseThrow(
        () -> new UserNotFoundException("User with username: " + username + " not found")
      );

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    if (!jwtService.isTokenValid(oldToken, userDetails)) {
      throw new TokenDataException();
    }

    refreshTokenService.find(oldToken);
    String newAccess = jwtService.generateAccessToken(user);
    String newRefresh = jwtService.generateRefreshToken(user);

    refreshTokenService.delete(oldToken);
    refreshTokenService.save(newRefresh, user.getId());

    return new AuthResponse(newAccess, newRefresh);
  }
}
