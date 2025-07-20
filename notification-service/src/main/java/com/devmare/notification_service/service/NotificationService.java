package com.devmare.notification_service.service;

import com.devmare.notification_service.event.AsteroidCollisionEvent;

public interface NotificationService {

    void listenAsteroidCollisionEvent(AsteroidCollisionEvent event);
}
