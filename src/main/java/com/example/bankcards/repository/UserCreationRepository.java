package com.example.bankcards.repository;

import com.example.bankcards.entity.UserCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCreationRepository extends JpaRepository<UserCreationRequest, Long> {

}
