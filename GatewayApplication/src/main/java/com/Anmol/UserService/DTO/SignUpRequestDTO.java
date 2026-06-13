package com.Anmol.UserService.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {
    String email;
    String emailOtp;
    String password;
}
