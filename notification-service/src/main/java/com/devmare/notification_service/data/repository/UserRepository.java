package com.devmare.notification_service.data.repository;

import com.devmare.notification_service.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsNotificationEnabledTrue();
}
