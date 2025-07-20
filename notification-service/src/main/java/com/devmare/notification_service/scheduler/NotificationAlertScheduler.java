package com.devmare.notification_service.scheduler;

import com.devmare.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationAlertScheduler {

    private final EmailService emailService;

    @Scheduled(fixedRate = 10000) // 10 seconds
    public void sendAlertingEmail() {
        log.info("Triggering scheduler to send email...");
        emailService.sendAsteroidAlertEmail();
    }
}
