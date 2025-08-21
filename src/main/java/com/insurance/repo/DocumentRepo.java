package com.insurance.repo;


import com.insurance.dto.EntityType;
import com.insurance.model.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DocumentRepo extends JpaRepository<Document, UUID> {

    // 1️⃣ Find documents by Entity ID and Entity Type (Claim, Policy, User)
    List<Document> findByEntityIdAndEntityType(UUID entityId, EntityType entityType);

    // 2️⃣ Find document by Document ID
    Document findByDocumentId(UUID documentId);

    // 3️⃣ Find documents by status (for admin verification dashboard)
    List<Document> findByStatus(com.insurance.dto.DocumentStatus status);
}
