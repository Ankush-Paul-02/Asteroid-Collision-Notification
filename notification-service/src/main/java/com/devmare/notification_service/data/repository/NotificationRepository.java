package com.devmare.notification_service.data.repository;

import com.devmare.notification_service.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByIsNotificationSentIsFalse();
}
