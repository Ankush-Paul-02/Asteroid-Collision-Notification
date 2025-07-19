package com.devmare.asteroid_alert.client;

import com.devmare.asteroid_alert.dto.Asteroid;
import com.devmare.asteroid_alert.dto.NasaNeoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NasaClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final WebClient webClient;

    @Value("${nasa.api-key}")
    private String API_KEY;

    @Value("${nasa.url}")
    private String URL;

    public Mono<List<Asteroid>> getNeoAstreoids(LocalDate fromDate, LocalDate toDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("neo/rest/v1/feed")
                        .queryParam("start_date", FORMATTER.format(fromDate))
                        .queryParam("end_date", FORMATTER.format(toDate))
                        .queryParam("api_key", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(NasaNeoResponse.class)
                .map(response -> {
                    if (response == null || response.getNearEarthObjects() == null) {
                        return Collections.emptyList();
                    }
                    return response.getNearEarthObjects().values().stream()
                            .flatMap(List::stream)
                            .toList();
                });
    }
}
