package com.insurance.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.insurance.dto.DocumentStatus;
import com.insurance.dto.DocumentType;
import com.insurance.dto.EntityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documents")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID documentId;

    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize; 

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private UUID entityId;  // Foreign key reference to Claim/Policy/User

    private UUID uploadedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20) 
    private DocumentStatus status;  // PENDING_VERIFICATION, VERIFIED, REJECTED

    private String remarks;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
