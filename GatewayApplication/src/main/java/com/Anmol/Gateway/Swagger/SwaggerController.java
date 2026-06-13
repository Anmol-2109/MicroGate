package com.Anmol.Gateway.Swagger;



import com.Anmol.Gateway.Entity.RouteEntity;
import com.Anmol.Gateway.Repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SwaggerController {

    private final RouteRepository routeRepository;

    @GetMapping("/swagger/services")
    public List<SwaggerServiceDto> getSwaggerServices() {

        return routeRepository.findAll()
                .stream()
                .map(route -> {

                    String prefix =
                            route.getPathPattern()
                                    .replace("/**", "");

                    return new SwaggerServiceDto(
                            route.getServiceName(),
                            prefix + "/v3/api-docs"
                    );
                })
                .toList();
    }
}