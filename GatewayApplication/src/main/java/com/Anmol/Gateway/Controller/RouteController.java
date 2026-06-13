package com.Anmol.Gateway.Controller;



import com.Anmol.Gateway.DTO.RouteRequestDto;
import com.Anmol.Gateway.Entity.RouteEntity;
import com.Anmol.Gateway.Service.RouteRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteRegistryService service;

    @PostMapping
    public RouteEntity createRoute(
            @RequestBody RouteRequestDto dto
    ) {

        return service.createRoute(dto);
    }

    @GetMapping
    public List<RouteEntity> getAllRoutes() {

        return service.getAllRoutes();
    }
}