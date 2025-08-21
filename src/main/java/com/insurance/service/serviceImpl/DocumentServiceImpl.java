package com.insurance.service.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.dto.DocumentStatus;
import com.insurance.dto.EntityType;
import com.insurance.dto.Document.DocumentRequestDTO;
import com.insurance.dto.Document.DocumentResponseDTO;
import com.insurance.events.EventPublisher;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.Document;
import com.insurance.model.EventMessage;
import com.insurance.model.User;
import com.insurance.repo.DocumentRepo;
import com.insurance.repo.PolicyRepo;
import com.insurance.repo.UserRepo;
import com.insurance.service.DocumentService;
import com.insurance.util.GenericMapper;

@Service
public class DocumentServiceImpl  implements DocumentService{


    private final DocumentRepo documentRepo;
    private final GenericMapper genericMapper;
    private final UserRepo userRepo;
    private final PolicyRepo policyRepo;
    private final EventPublisher eventPublisher;

    public DocumentServiceImpl(DocumentRepo documentRepo, GenericMapper genericMapper, UserRepo userRepo, PolicyRepo policyRepo, EventPublisher eventPublisher) {
        this.documentRepo = documentRepo;
        this.genericMapper = genericMapper;
        this.userRepo = userRepo;
        this.policyRepo = policyRepo;
        this.eventPublisher = eventPublisher;
    }


   @Override
public DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDto, MultipartFile file) {

    // 1. Validate Entity Exists or Not
    switch (requestDto.getEntityType()) {
    case POLICY:
        if (!policyRepo.existsById(requestDto.getEntityId())) {
            throw new ResourceNotFoundException("Policy not found with ID: " + requestDto.getEntityId());
        }
        break;
    case CLAIM:
    throw new UnsupportedOperationException("Claim Entity validation is not yet implemented.");

  case USER:
        if (!userRepo.existsById(requestDto.getEntityId())) {
            throw new ResourceNotFoundException("User not found with ID: " + requestDto.getEntityId());
        }
        break;
    default:
        throw new IllegalArgumentException("Invalid Entity Type: " + requestDto.getEntityType());
}
    // 2. Validate UploadedBy User Exists
    User uploadedBy = userRepo.findById(requestDto.getUploadedByUserId())
            .orElseThrow(() -> new ResourceNotFoundException("UploadedBy User not found with ID: " + requestDto.getUploadedByUserId()));

    // 3. Map DTO to Entity
   Document document = genericMapper.mapToEntity(requestDto, Document.class);
document.setDocumentId(null); // Force Hibernate to INSERT
document.setUploadedByUserId(uploadedBy.getUserId());
document.setFileName(file.getOriginalFilename());
document.setFileSize(file.getSize());
document.setFileType(file.getContentType());
document.setStatus(DocumentStatus.UPLOADED);
document.setFileUrl("https://dummy-storage-url.com/" + file.getOriginalFilename());
document.setRemarks("Uploaded via API");
document.setCreatedAt(LocalDateTime.now());
document.setUpdatedAt(LocalDateTime.now());



    Document savedDocument = documentRepo.save(document);

    // DocumentService.java (upload/verify/reject)
String ev = switch (document.getStatus()) {
  case PENDING_VERIFICATION -> "DOCUMENT_UPLOADED";
  case VERIFIED -> "DOCUMENT_VERIFIED";
  case REJECTED -> "DOCUMENT_REJECTED";
    default -> throw new IllegalArgumentException("Unexpected value: " + document.getStatus());
};
eventPublisher.publishAfterCommit("insurance.document.events",
  EventMessage.builder()
    .eventId(UUID.randomUUID())
    .eventType(ev)
    .aggregateType("DOCUMENT")
    .aggregateId(document.getDocumentId())
    .userId(document.getUploadedByUserId())
    .occurredAt(LocalDateTime.now())
    .source("document-service")
    .data(Map.of(
        "fileName", document.getFileName(),
        "documentType", document.getDocumentType().name(),
        "entityType", document.getEntityType().name()
    ))
    .build()
);


    return genericMapper.mapToDto(savedDocument, DocumentResponseDTO.class);
}


@Override
public DocumentResponseDTO getDocumentById(UUID documentId) {
    Document document = documentRepo.findById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));
    return genericMapper.mapToDto(document, DocumentResponseDTO.class);
}


@Override
public List<DocumentResponseDTO> getDocumentsByEntity(UUID entityId, String entityType) {
    EntityType type = EntityType.valueOf(entityType.toUpperCase());
    List<Document> documents = documentRepo.findByEntityIdAndEntityType(entityId, type);
    return documents.stream()
            .map(doc -> genericMapper.mapToDto(doc, DocumentResponseDTO.class))
            .collect(Collectors.toList());
}


@Override
public DocumentResponseDTO updateDocumentStatus(UUID documentId, String status, String remarks) {
    Document document = documentRepo.findById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));

    document.setStatus(DocumentStatus.valueOf(status.toUpperCase()));
    document.setRemarks(remarks);
    document.setUpdatedAt(LocalDateTime.now());

    

    Document updated = documentRepo.save(document);
    return genericMapper.mapToDto(updated, DocumentResponseDTO.class);
}


@Override
public void deleteDocument(UUID documentId) {
    Document document = documentRepo.findById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));

    documentRepo.delete(document);
}


@Override
public String getDocumentDownloadUrl(UUID documentId) {
    Document document = documentRepo.findById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));
    return document.getFileUrl();
}




    
}
