package com.Anmol.UserService.Service;

import com.Anmol.UserService.DTO.LoginRequestDTO;
import com.Anmol.UserService.DTO.OtpRequestDTO;
import com.Anmol.UserService.DTO.SignUpRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Object sendOtp(OtpRequestDTO dto);
    Object signUp(SignUpRequestDTO dto);

    Object login(LoginRequestDTO dto);
}
