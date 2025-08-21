package com.insurance.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// NotificationPreference.java
@Entity
@Table(name="notification_preferences")
@Data
public class NotificationPreference {
  @Id
  private UUID userId;
  private boolean emailEnabled = true;
  private boolean smsEnabled = true;
  private boolean inAppEnabled = true;
  private String email; // override if needed
  private String phone;
}
