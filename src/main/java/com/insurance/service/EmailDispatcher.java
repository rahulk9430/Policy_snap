package com.insurance.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

// EmailDispatcher.java
@Service
@RequiredArgsConstructor
public class EmailDispatcher {
  private final JavaMailSender mailSender;

  public void send(String to, String subject, String html) {
    MimeMessage msg = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(msg, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(html, true);
      mailSender.send(msg);
    } catch (Exception e) { throw new RuntimeException(e); }
  }
}
