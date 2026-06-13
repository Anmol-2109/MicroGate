package com.Anmol.Gateway.Swagger;

import com.Anmol.Gateway.Repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SwaggerConfigController {

    private final RouteRepository routeRepository;

    @GetMapping("/swagger-config")
    public Map<String, Object> swaggerConfig() {

        List<Map<String, String>> urls =
                new ArrayList<>();


        // Gateway Swagger

        Map<String, String> gateway =
                new HashMap<>();

        gateway.put(
                "name",
                "GATEWAY"
        );

        gateway.put(
                "url",
                "/v3/api-docs"
        );

        urls.add(
                gateway
        );


        // Dynamic Microservices

        urls.addAll(
                routeRepository.findAll()
                        .stream()
                        .map(route -> {

                            String prefix =
                                    route.getPathPattern()
                                            .replace("/**", "");

                            Map<String, String> entry =
                                    new HashMap<>();

                            entry.put(
                                    "name",
                                    route.getServiceName()
                            );

                            entry.put(
                                    "url",
                                    prefix + "/v3/api-docs"
                            );

                            return entry;
                        })
                        .toList()
        );

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "urls",
                urls
        );

        return result;
    }
}