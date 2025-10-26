package com.nagp.ms.userService.dao;

import com.nagp.ms.userService.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpDao extends JpaRepository<Otp, String> {
    Optional<Otp> findByEmailAndCodeAndUsedFalse(String email, String code);
}