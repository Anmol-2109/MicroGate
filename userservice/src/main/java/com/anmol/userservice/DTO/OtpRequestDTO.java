package com.anmol.userservice.DTO;



import com.anmol.userservice.Entity.Type.OtpPurpose;
import lombok.*;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequestDTO {


    private String email;
    private OtpPurpose purpose;
}