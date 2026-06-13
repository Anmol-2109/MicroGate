package com.Anmol.UserService.DTO;

import com.Anmol.UserService.Entity.Type.OtpPurpose;
import lombok.*;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequestDTO {

    private String phoneNumber;

    private String email;

    private OtpPurpose purpose;
}