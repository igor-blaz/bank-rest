package com.example.bankcards.entity;

import com.example.bankcards.enums.RequestStatus;
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
    private String email;
    private Integer age;
    @Column(name = "password_hash")
    private String password;
    @Column(name = "status")
    @Setter
    @Enumerated(EnumType.STRING)
    private RequestStatus userRequestStatus;
}
