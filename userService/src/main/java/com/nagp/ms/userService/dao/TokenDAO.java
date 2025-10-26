package com.nagp.ms.userService.dao;

import com.nagp.ms.userService.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenDAO extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
}
