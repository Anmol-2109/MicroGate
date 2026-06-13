package com.Anmol.UserService.Controller;

import com.Anmol.UserService.DTO.LoginRequestDTO;
import com.Anmol.UserService.DTO.OtpRequestDTO;
import com.Anmol.UserService.DTO.SignUpRequestDTO;
import com.Anmol.UserService.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDTO dto) {
        return ResponseEntity.ok(userService.sendOtp(dto));
    }

    @PostMapping("/signup/rider")
    public ResponseEntity<?> riderSignup(@RequestBody SignUpRequestDTO dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                userService.login(dto)
        );
    }
}