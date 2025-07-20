package com.devmare.notification_service.service;

import com.devmare.notification_service.data.entity.Notification;
import com.devmare.notification_service.data.repository.NotificationRepository;
import com.devmare.notification_service.event.AsteroidCollisionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @KafkaListener(topics = "asteroid-alert", groupId = "asteroid-alert")
    public void listenAsteroidCollisionEvent(AsteroidCollisionEvent event) {
        log.info("Received event: {}", event);

        Notification notification = Notification.builder()
                .asteroidName(event.getAsteroidName())
                .closeApproachDate(LocalDate.parse(event.getCloseApproachDate()))
                .estimatedDiameterAvgMeters(event.getEstimatedDiameterAvgMeters())
                .missDistanceKilometers(new BigDecimal(event.getMissDistanceKilometers()))
                .isNotificationSent(false)
                .build();

        notification = notificationRepository.save(notification);
        log.info("Saved notification: {}", notification);
    }
}
