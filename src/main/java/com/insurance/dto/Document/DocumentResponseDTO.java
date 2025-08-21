package com.insurance.dto.Document;

import java.time.LocalDateTime;
import java.util.UUID;

import com.insurance.dto.DocumentStatus;
import com.insurance.dto.DocumentType;
import com.insurance.dto.EntityType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {

    private UUID documentId;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;

    private DocumentType documentType;
    private EntityType entityType;
    private UUID entityId;

    private UUID uploadedByUserId;
    private DocumentStatus status;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

