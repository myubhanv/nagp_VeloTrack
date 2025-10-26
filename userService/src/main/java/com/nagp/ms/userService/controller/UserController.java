package com.nagp.ms.userService.controller;

import com.nagp.ms.userService.dto.UserDTO;
import com.nagp.ms.userService.dto.UserLoginRequest;
import com.nagp.ms.userService.dto.UserRegisterRequest;
import com.nagp.ms.userService.model.User;
import com.nagp.ms.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest credentials) {
        return userService.login(credentials.getUsername(),credentials.getPassword());
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout (@RequestBody Map<String, String> request) {
        String username = request.get("username");
        return userService.logout(username);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");
        return userService.refresh(refreshTokenStr);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterRequest createUserParameters) {
        User user = User.builder()
                .username(createUserParameters.getUsername())
                .email(createUserParameters.getEmail())
                .password(createUserParameters.getPassword())
                .phone(createUserParameters.getPhone())
                .build();
        User saved = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.from(saved));
    }

    @PostMapping("/passwordResetRequest")
    public ResponseEntity<?> passwordResetRequest(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return userService.requestPasswordReset(email);
    }

    @PostMapping("/passwordResetConfirm")
    public ResponseEntity<?> passwordResetConfirm(@RequestBody Map<String, String> request) {
        return userService.confirmPasswordReset(request);
    }

    @GetMapping("/details")
    public User getUserDetails(String username) {
        return userService.findByUsername(username).orElseThrow();
    }

}
