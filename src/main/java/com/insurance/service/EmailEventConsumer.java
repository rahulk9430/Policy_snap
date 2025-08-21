package com.insurance.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.insurance.model.EventMessage;

import java.util.Map;

@Service
public class EmailEventConsumer {

    private final JavaMailSender mailSender;

    public EmailEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "insurance.user.events", groupId = "email-service")
    public void consumeUserRegisteredEvent(EventMessage eventMessage) {
        if ("USER_REGISTERED".equals(eventMessage.getEventType())) {
            Map<String, Object> data = eventMessage.getData();

            String email = (String) data.get("email");
            String username = (String) data.get("username");

            // Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Insurance App");
            message.setText("Hello " + username + ",\n\nThank you for registering with us!");

            mailSender.send(message);

            System.out.println("✅ Email sent to: " + email);
        }
    }
}
