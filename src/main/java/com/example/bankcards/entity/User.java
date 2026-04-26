package com.example.bankcards.entity;

import com.example.bankcards.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
}
