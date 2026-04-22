package com.example.bankcards.entity;

import com.example.bankcards.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "card_requests")
public class CardCreationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(name = "status")
    @Setter
    @Enumerated(EnumType.STRING)
    private RequestStatus cardRequestStatus;
}
