package com.devmare.asteroid_alert.service;

import com.devmare.asteroid_alert.client.NasaClient;
import com.devmare.asteroid_alert.dto.Asteroid;
import com.devmare.asteroid_alert.event.AsteroidCollisionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsteroidAlertServiceImpl implements AsteroidAlertService {

    private final NasaClient nasaClient;
    private final KafkaTemplate<String, AsteroidCollisionEvent> kafkaTemplate;

    @Override
    public Mono<Void> alert() {
        log.info("🌠 Asteroid alert service is running...");

        final LocalDate fromDate = LocalDate.now();
        final LocalDate toDate = LocalDate.now().plusDays(7);

        return nasaClient.getNeoAstreoids(fromDate, toDate)
                .doOnNext(asteroids -> {
                    log.info("Received {} asteroids", asteroids.size());
                })
                .map(asteroids -> asteroids.stream()
                        .filter(Asteroid::isPotentiallyHazardousAsteroid)
                        .toList())
                .doOnNext(hazardous -> log.info("Found {} hazardous asteroids", hazardous.size()))
                .map(this::createEventListOfDangerousAsteroids)
                .doOnNext(events -> {
                    log.info("Sending {} events", events.size());
                    events.forEach(event -> {
                        kafkaTemplate.send("asteroid-alert", event);
                        log.info("Event sent: {}", event);
                    });
                })
                .then();
    }

    private List<AsteroidCollisionEvent> createEventListOfDangerousAsteroids(List<Asteroid> hazardousAsteroids) {
        return hazardousAsteroids.stream()
                .map(asteroid -> {
                    if (asteroid.isPotentiallyHazardousAsteroid()) {
                        return AsteroidCollisionEvent.builder()
                                .asteroidName(asteroid.getName())
                                .closeApproachDate(asteroid.getCloseApproachData().getFirst().getCloseApproachDate().toString())
                                .missDistanceKilometers(asteroid.getCloseApproachData().getFirst().getMissDistance().getKilometers())
                                .estimatedDiameterAvgMeters(
                                        (asteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMin() +
                                                asteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMax()) / 2
                                )
                                .build();
                    }
                    return null;
                }).toList();
    }
}
