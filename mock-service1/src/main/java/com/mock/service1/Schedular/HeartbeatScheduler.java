package com.mock.service1.Schedular;




import com.mock.service1.DTO.HeartbeatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatScheduler {

    private final RestTemplate restTemplate;

    @Value("${server.port}")
    private String port;

    @Scheduled(fixedRate = 15000)
    public void sendHeartbeat() {

        try {

            HeartbeatRequest request =
                    new HeartbeatRequest(
                            "service1-"+port
                    );

            restTemplate.postForObject(
                    "http://localhost:8080/services/heartbeat",
                    request,
                    Void.class
            );

            log.info(
                    "Heartbeat sent for service1-a"
            );

        } catch (Exception ex) {

            log.error(
                    "Heartbeat failed",
                    ex
            );
        }
    }
}