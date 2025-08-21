package com.insurance.repo;

import com.insurance.model.*;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
    // This interface extends JpaRepository to provide CRUD operations for Notification entities.
    // Additional methods can be defined here as needed.
    
}
