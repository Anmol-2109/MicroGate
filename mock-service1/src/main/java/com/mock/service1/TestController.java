package com.mock.service1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("request comes");
        return "SERVICE-1 "+port;
    }

    @GetMapping("/instance")
    public String instance() {
        return "INSTANCE-A"+port;
    }
}