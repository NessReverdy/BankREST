package com.example.bankcards.entity;

import com.example.bankcards.status.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  private String middleName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "owner",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
  private Set<CardEntity> cards = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private UserRole role;
}
