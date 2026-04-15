package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String cardNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
    private LocalDate expirationDate;
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;
    @Column(nullable = false)
    private BigDecimal balance;

    public String getMaskedNumber() {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

}
