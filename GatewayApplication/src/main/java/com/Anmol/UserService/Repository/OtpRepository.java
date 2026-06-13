package com.Anmol.UserService.Repository;

import com.Anmol.UserService.Entity.OtpVerification;
import com.Anmol.UserService.Entity.Type.OtpPurpose;
import com.Anmol.UserService.Entity.Type.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification , Long> {


    List<OtpVerification> findActiveOtps(String email, String phoneNumber, OtpPurpose purpose, OtpStatus otpStatus);

    Optional<OtpVerification> findValidOtp(String email, String emailOtp, OtpPurpose otpPurpose, OtpStatus otpStatus);
}
