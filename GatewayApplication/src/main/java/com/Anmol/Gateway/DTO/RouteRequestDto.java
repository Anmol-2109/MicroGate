package com.Anmol.Gateway.DTO;



import lombok.Data;


@Data
public class RouteRequestDto {

    private String pathPattern;

    private Boolean enabled;

    private String serviceName;
}