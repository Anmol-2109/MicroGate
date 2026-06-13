package com.mock.service1.DTO;


public class HeartbeatRequest {

    private String instanceId;

    public HeartbeatRequest() {
    }

    public HeartbeatRequest(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}