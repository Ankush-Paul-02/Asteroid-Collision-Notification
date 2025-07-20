package com.devmare.notification_service.service;

import com.devmare.notification_service.data.entity.Notification;
import com.devmare.notification_service.data.entity.User;
import com.devmare.notification_service.data.repository.NotificationRepository;
import com.devmare.notification_service.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;
    @Value("${email.service.from.email}")
    private String fromEmail;

    @Async
    @Override
    public void sendAsteroidAlertEmail() {
        log.info("Sending asteroid alert email...");
        final String text = createEmailText();
        if (text == null) {
            log.info("No notifications to send.");
            return;
        }

        List<String> emails = userRepository.findByIsNotificationEnabledTrue().stream().map(User::getEmail).toList();
        if (emails.isEmpty()) {
            log.info("No users to send notifications to.");
            return;
        }

        emails.forEach(email -> sendEmail(email, text));
        log.info("Email sent successfully!");
    }

    private void sendEmail(String toEmail, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("Nasa Asteroid Collision Alert");
        message.setText(text);

        log.info("Sending email to: {}", toEmail);
        javaMailSender.send(message);
    }

    private String createEmailText() {
        List<Notification> notifications = notificationRepository.findAllByIsNotificationSentIsFalse();

        if (notifications.isEmpty()) {
            return null;
        }

        StringBuilder emailText = new StringBuilder();
        emailText.append("Asteroid Collision Alerts:\n\n");

        for (Notification notification : notifications) {
            emailText.append("Asteroid Name: ").append(notification.getAsteroidName()).append("\n");
            emailText.append("Close Approach Date: ").append(notification.getCloseApproachDate()).append("\n");
            emailText.append("Miss Distance (km): ").append(notification.getMissDistanceKilometers()).append("\n");
            emailText.append("Estimated Diameter (m): ").append(notification.getEstimatedDiameterAvgMeters()).append("\n\n");
            notification.setNotificationSent(true);
            notificationRepository.save(notification);
        }
        log.info("Email text: {}", emailText);
        return emailText.toString();
    }
}
