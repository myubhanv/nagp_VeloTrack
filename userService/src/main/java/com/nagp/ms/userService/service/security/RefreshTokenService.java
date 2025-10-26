package com.nagp.ms.userService.service.security;

import com.nagp.ms.userService.dao.TokenDAO;
import com.nagp.ms.userService.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenDAO tokenDAO;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .username(username)
                .expiryDate(Instant.now().plusSeconds(60 * 60 * 24)) // 24h
                .build();
        return tokenDAO.save(refreshToken);
    }

    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            tokenDAO.delete(token);
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return tokenDAO.findByToken(token);
    }

    public void logout(String username) {
        tokenDAO.findAll()
                .stream()
                .filter(rt -> rt.getUsername().equals(username))
                .forEach(tokenDAO::delete);
    }
}
