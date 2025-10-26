package com.nagp.ms.userService.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
}
