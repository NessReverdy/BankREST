package com.example.bankcards.security.jwt.service;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(username));

    return User
      .withUsername(userEntity.getUsername())
      .password(userEntity.getPassword())
      .authorities("ROLE_" + userEntity.getRole().name())
      .build();
  }
}
