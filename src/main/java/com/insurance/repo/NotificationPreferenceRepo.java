package com.insurance.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.model.NotificationPreference;

public interface NotificationPreferenceRepo  extends JpaRepository<NotificationPreference, UUID> {
    // This interface extends JpaRepository to provide CRUD operations for NotificationPreference entities.
    // Additional methods can be defined here as needed.

    
}