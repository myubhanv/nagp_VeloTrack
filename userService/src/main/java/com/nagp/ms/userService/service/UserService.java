package com.nagp.ms.userService.service;

import com.nagp.ms.userService.dao.UserDAO;
import com.nagp.ms.userService.model.User;
import com.nagp.ms.userService.service.security.JwtService;
import com.nagp.ms.userService.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;

    public ResponseEntity<?> login(String username, String password) {
        return userDao.findByUsername(username)
                .map(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
                    }
                    String token = jwtService.generateToken(username);
                    var refreshToken = refreshTokenService.createRefreshToken(username);

                    return ResponseEntity.ok(Map.of(
                            "accessToken", token,
                            "refreshToken", refreshToken.getToken()
                    ));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error", "User not found")));
    }

    public ResponseEntity<?> logout (String username){
        refreshTokenService.logout(username);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
}

    public ResponseEntity<?> refresh(String token) {
        var tokenOpt = refreshTokenService.findByToken(token)
                .flatMap(refreshTokenService::verifyExpiration);

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired refresh token"));
        }

        String username = tokenOpt.get().getUsername();
        String newAccessToken = jwtService.generateToken(username);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    public User register(User user) {
        if (userDao.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("username exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    public ResponseEntity<?> requestPasswordReset(String email) {
        var userOpt = userDao.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        otpService.generateOtp(email);
        return ResponseEntity.ok(Map.of("message", "Password reset OTP sent to email"));
    }

    public ResponseEntity<?> confirmPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
        }

        var user = userDao.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }
    public Optional<User> findByUsername(String name) {
        return userDao.findByUsername(name);
    }


}
