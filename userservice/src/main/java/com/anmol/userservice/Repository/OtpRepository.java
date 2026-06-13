package com.anmol.userservice.Repository;


import com.anmol.userservice.Entity.OtpVerification;
import com.anmol.userservice.Entity.Type.OtpPurpose;
import com.anmol.userservice.Entity.Type.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    @Query("""
    SELECT o
    FROM OtpVerification o
    WHERE
        o.email = :email
        AND o.purpose = :purpose
        AND o.revoked = false
        AND o.status = :status
""")
    List<OtpVerification> findActiveOtps(

            @Param("email")
            String email,

            @Param("purpose")
            OtpPurpose purpose,

            @Param("status")
            OtpStatus otpStatus
    );

    @Query("""
    SELECT o
    FROM OtpVerification o
    WHERE
        o.email = :email
        AND o.otpCode = :otpCode
        AND o.purpose = :purpose
        AND o.status = :status
        AND o.revoked = false
""")
    Optional<OtpVerification> findValidOtp(
            @Param("email") String email,
            @Param("otpCode") String otpCode,
            @Param("purpose") OtpPurpose purpose,
            @Param("status") OtpStatus status
    );
}
