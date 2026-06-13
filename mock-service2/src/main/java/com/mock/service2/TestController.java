package com.mock.service2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${server.port}")
    private String port;

    private int counter = 0 ;
    @GetMapping("/hello")
    public String hello() {
        return "SERVICE-2"+port;
    }

    @GetMapping("/instance")
    public String instance() {
        return "INSTANCE-A"+port;
    }

    @GetMapping("/fail")
    public String error() {
        System.out.println("FAIL ENDPOINT HIT");
        throw new RuntimeException("boom");
    }

    @GetMapping("/abc")
    public String abc() {
        return port+" - abc Executed";
    }

    @GetMapping("/retry-test")
    public String retryTest() {

        counter++;

        System.out.println(
                "Retry Test Count = " + counter
        );

        throw new RuntimeException(
                "Force Retry"
        );
    }

    @GetMapping("/retry-success")
    public String retrySuccess() {

        counter++;

        System.out.println(
                "Attempt = " + counter
        );

        if(counter < 3)
        {
            throw new RuntimeException(
                    "Temporary Failure"
            );
        }

        return "SUCCESS";
    }
}