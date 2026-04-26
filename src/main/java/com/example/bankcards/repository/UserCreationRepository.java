package com.example.bankcards.repository;

import com.example.bankcards.entity.UserCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreationRepository extends JpaRepository<UserCreationRequest, Long> {

}
