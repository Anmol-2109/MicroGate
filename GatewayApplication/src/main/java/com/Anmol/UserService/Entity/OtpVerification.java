package com.Anmol.UserService.Entity;



import com.Anmol.UserService.Entity.Type.OtpPurpose;
import com.Anmol.UserService.Entity.Type.OtpStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "otp_verifications",
        indexes = {
                @Index(name = "idx_otp_phone", columnList = "phoneNumber"),
                @Index(name = "idx_otp_email", columnList = "email")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerification  {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpStatus status = OtpStatus.PENDING;


    private String phoneNumber;

    private String email;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime verifiedAt;

    private Integer attempts = 0;

    private Integer maxAttempts = 5;

    private boolean revoked = false;
}