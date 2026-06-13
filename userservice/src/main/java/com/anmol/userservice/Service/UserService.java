package com.anmol.userservice.Service;

import com.anmol.userservice.DTO.LoginRequestDTO;
import com.anmol.userservice.DTO.OtpRequestDTO;
import com.anmol.userservice.DTO.SignUpRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Object sendOtp(OtpRequestDTO dto);
    Object signUp(SignUpRequestDTO dto);

    Object login(LoginRequestDTO dto);
}
