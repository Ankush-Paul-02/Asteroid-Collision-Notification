package com.devmare.asteroid_alert.controller;

import com.devmare.asteroid_alert.service.AsteroidAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/asteroid-alert")
public class AsteroidAlertController {

    private final AsteroidAlertService asteroidAlertService;

    @PostMapping("/alert")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> alert() {
        return asteroidAlertService.alert();
    }
}
