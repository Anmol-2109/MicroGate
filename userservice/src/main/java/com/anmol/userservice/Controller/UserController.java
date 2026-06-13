package com.anmol.userservice.Controller;


import com.anmol.userservice.DTO.LoginRequestDTO;
import com.anmol.userservice.DTO.OtpRequestDTO;
import com.anmol.userservice.DTO.SignUpRequestDTO;
import com.anmol.userservice.Service.UserService;
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
        System.out.println("controller start");
        String res = userService.sendOtp(dto).toString();
        System.out.println("controller end");
        return ResponseEntity.ok(res);
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