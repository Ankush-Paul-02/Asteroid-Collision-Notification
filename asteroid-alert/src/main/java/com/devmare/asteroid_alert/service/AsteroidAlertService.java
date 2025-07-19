package com.devmare.asteroid_alert.service;

import reactor.core.publisher.Mono;

public interface AsteroidAlertService {

    Mono<Void> alert();
}
