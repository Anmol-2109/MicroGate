package com.Anmol.Gateway.Routing;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteRefreshService {

    private final ApplicationEventPublisher publisher;

    public void refreshRoutes() {

        publisher.publishEvent(
                new RefreshRoutesEvent(this)
        );
    }
}