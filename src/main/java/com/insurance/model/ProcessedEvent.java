package com.insurance.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// ProcessedEvent.java
@Entity
@Table(name="processed_events")
@Data
public class ProcessedEvent {
  @Id
  private UUID eventId;
  private LocalDateTime processedAt = LocalDateTime.now();
}
