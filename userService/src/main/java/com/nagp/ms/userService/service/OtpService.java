package com.nagp.ms.userService.service;

import com.nagp.ms.userService.dao.OtpDao;
import com.nagp.ms.userService.model.Otp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpDao otpRepository;

    public String generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        Otp otp = Otp.builder()
                .email(email)
                .code(code)
                .expiry(Instant.now().plusSeconds(5 * 60)) // 5 min
                .used(false)
                .build();
        otpRepository.save(otp);

        // TODO: Replace with real email integration
        System.out.println("OTP for " + email + " is: " + code);

        return code;
    }

    public boolean validateOtp(String email, String code) {
        Optional<Otp> otpOpt = otpRepository.findByEmailAndCodeAndUsedFalse(email, code);
        if (otpOpt.isEmpty()) return false;

        Otp otp = otpOpt.get();
        if (otp.getExpiry().isBefore(Instant.now())) return false;

        otp.setUsed(true);
        otpRepository.save(otp);
        return true;
    }
}