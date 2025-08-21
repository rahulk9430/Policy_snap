package com.insurance.service;

import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.insurance.repo.UserRepo;

@Service
public class EmailService {

    private final UserRepo userRepo;
    private final JavaMailSender mailSender;

    public EmailService(UserRepo userRepo, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    public void sendEmail(UUID userId, String subject, String body) {
        String to = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("✅ Email sent to: " + to);
    }
}
