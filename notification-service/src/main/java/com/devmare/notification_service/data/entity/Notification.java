package com.devmare.notification_service.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asteroidName;
    private LocalDate closeApproachDate;
    private BigDecimal missDistanceKilometers;
    private double estimatedDiameterAvgMeters;
    private boolean isNotificationSent;
}
