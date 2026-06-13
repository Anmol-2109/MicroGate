package com.anmol.userservice.Schedular;





import com.anmol.userservice.DTO.HeartbeatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatScheduler {

    private final RestTemplate restTemplate;

    @Scheduled(fixedRate = 15000)
    public void sendHeartbeat() {

        try {

            HeartbeatRequest request =
                    new HeartbeatRequest(
                            "user-service-1"
                    );

            restTemplate.postForObject(
                    "http://localhost:8080/services/heartbeat",
                    request,
                    Void.class
            );

            log.info(
                    "Heartbeat sent for user-service-1"
            );

        } catch (Exception ex) {

            log.error(
                    "Heartbeat failed",
                    ex
            );
        }
    }
}