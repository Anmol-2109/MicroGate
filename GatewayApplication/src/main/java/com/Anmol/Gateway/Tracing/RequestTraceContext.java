package com.Anmol.Gateway.Tracing;



import lombok.Data;

@Data
public class RequestTraceContext {

    private String routeId;

    private String serviceName;

    private String selectedInstance;

    private long startTime;

}