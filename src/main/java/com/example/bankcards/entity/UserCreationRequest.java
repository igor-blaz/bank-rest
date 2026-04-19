package com.example.bankcards.entity;

import com.example.bankcards.enums.Role;
import com.example.bankcards.enums.UserRequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_requests")
public class UserCreationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String password;
    @Column(name = "status")
    @Setter
    @Enumerated(EnumType.STRING)
    private UserRequestStatus userRequestStatus;


}
