package com.insurance.model;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="notifications")
@Data @NoArgsConstructor @AllArgsConstructor
public class Notification {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private UUID userId;
  @Enumerated(EnumType.STRING)
  private Channel channel; // EMAIL, SMS, IN_APP
  private String title;
  @Column(columnDefinition="TEXT")
  private String message;
  @Enumerated(EnumType.STRING)
  private Status status; // PENDING, SENT, FAILED
  private int retries;
  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime sentAt;

  public enum Status { PENDING, SENT, FAILED }
  public enum Channel { EMAIL, SMS, IN_APP }
}
