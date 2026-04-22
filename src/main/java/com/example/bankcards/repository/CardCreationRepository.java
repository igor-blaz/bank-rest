package com.example.bankcards.repository;

import com.example.bankcards.entity.CardCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCreationRepository extends JpaRepository<CardCreationRequest, Long> {

}
