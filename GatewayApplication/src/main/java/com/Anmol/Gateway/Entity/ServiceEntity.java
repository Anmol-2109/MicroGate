package com.Anmol.Gateway.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String instanceId;

    private String serviceUrl;

    private String serviceVersion;

    private String status;

    private LocalDateTime lastHeartbeat;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}