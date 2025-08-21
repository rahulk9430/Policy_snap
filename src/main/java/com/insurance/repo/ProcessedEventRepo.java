package com.insurance.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.model.ProcessedEvent;

public interface ProcessedEventRepo extends JpaRepository<ProcessedEvent, UUID> {
    // This interface extends JpaRepository to provide CRUD operations for ProcessedEvent entities.
    // Additional methods can be defined here as needed.

}
