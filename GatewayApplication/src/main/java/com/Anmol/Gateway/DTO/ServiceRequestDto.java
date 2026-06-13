package com.Anmol.Gateway.DTO;



import lombok.Data;

@Data
public class ServiceRequestDto {

    private String serviceName;

    private String serviceUrl;

    private String serviceVersion;

    private String status;
}