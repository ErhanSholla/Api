package com.example.ecommerceapi.repository;

import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
