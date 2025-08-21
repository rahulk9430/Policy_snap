package com.insurance.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// NotificationTemplate.java
@Entity
@Table(name="notification_templates")
@Data
public class NotificationTemplate {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String code;       // e.g. PAYMENT_SUCCESS_EMAIL
  private String subject;    // for EMAIL only
  @Column(columnDefinition="TEXT")
  private String body;       // FreeMarker/Thymeleaf text
  @Enumerated(EnumType.STRING)
  private Notification.Channel channel;
}
