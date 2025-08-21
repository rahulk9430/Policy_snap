package com.insurance.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.model.NotificationTemplate;

public interface NotificationTemplateRepo  extends JpaRepository<NotificationTemplate, UUID> {
    Optional<NotificationTemplate> findByCode(String code);
    
}
