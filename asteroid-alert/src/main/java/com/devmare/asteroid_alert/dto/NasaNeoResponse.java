package com.devmare.asteroid_alert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NasaNeoResponse {

    @JsonProperty("near_earth_objects")
    private Map<String, List<Asteroid>> nearEarthObjects;

    @JsonProperty("element_count")
    private Long totalAsteroids;
}
