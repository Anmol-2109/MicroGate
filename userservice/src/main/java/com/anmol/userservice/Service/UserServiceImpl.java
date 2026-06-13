package com.anmol.userservice.Service;


import com.anmol.userservice.DTO.*;
import com.anmol.userservice.Entity.OtpVerification;
import com.anmol.userservice.Entity.Type.OtpPurpose;
import com.anmol.userservice.Entity.Type.OtpStatus;
import com.anmol.userservice.Entity.User;
import com.anmol.userservice.Repository.OtpRepository;
import com.anmol.userservice.Repository.UserRepository;
import com.anmol.userservice.Security.AuthUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil securityUtil;
    private final JavaMailSender mailSender;

    @Override
    public String sendOtp(OtpRequestDTO dto) {

        System.out.println("enter service");
        revokePreviousOtps(dto);
        String emailOtp = generateOtp();

        System.out.println("otp generated");
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(dto.getEmail());
        otpVerification.setOtpCode(emailOtp);
        otpVerification.setPurpose(dto.getPurpose());
        otpVerification.setStatus(OtpStatus.PENDING);
        otpVerification.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );

        OtpVerification otp = otpRepository.save(otpVerification);

        System.out.println("email otp done");
        sendEmailOtp(dto.getEmail(), emailOtp);

        return "OTP sent successfully";
    }

    private String generateOtp() {
        return String.valueOf(
                100000 + new Random().nextInt(900000)
        );
    }

    private void revokePreviousOtps(OtpRequestDTO dto) {
        List<OtpVerification> oldOtps =
                otpRepository.findActiveOtps(
                        dto.getEmail(),
                        dto.getPurpose(),
                        OtpStatus.PENDING
                );

        oldOtps.forEach(otp -> otp.setRevoked(true));
        otpRepository.saveAll(oldOtps);
    }

    private void sendEmailOtp(String email, String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("RideFlux OTP Verification");
        message.setText(
                "Your OTP is: " + otp +
                        "\nValid for 5 minutes."
        );

        try {

            mailSender.send(message);

            System.out.println("EMAIL SENT");

        } catch (Exception e) {

            System.out.println("EMAIL FAILED");

            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO dto) {

        // verify email OTP
        OtpVerification emailOtp =
                otpRepository
                        .findValidOtp(
                                dto.getEmail(),
                                dto.getEmailOtp(),
                                OtpPurpose.AUTHORIZATION,
                                OtpStatus.PENDING
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Invalid email OTP")
                        );


        if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Email OTP expired"
            );
        }


        emailOtp.setStatus(OtpStatus.VERIFIED);
        emailOtp.setVerifiedAt(LocalDateTime.now());


        otpRepository.save(emailOtp);

        if (userRepository.existsByUsername(dto.getEmail())) {

            throw new RuntimeException("Email already registered");
        }


        // create user
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        User savedUser = userRepository.save(user);


        SignUpResponseDTO response = new SignUpResponseDTO();
        response.setEmail(user.getUsername());
        response.setUserId(savedUser.getId());
        response.setMessage("Signup Successfully Completed !!! ");

        return response;

    }

    @Override
    public Object login(LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );

        User user = (User)authentication.getPrincipal();

        String token = securityUtil.generateAccessToken(user);


        return LoginResponse.builder()
                .userId(user.getId())
                .accessToken(token)
                .build();

    }
}
