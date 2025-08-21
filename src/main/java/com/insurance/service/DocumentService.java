package com.insurance.service;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.dto.Document.DocumentRequestDTO;
import com.insurance.dto.Document.DocumentResponseDTO;


public interface DocumentService {

    // 1️⃣ Upload Document
    DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDto, MultipartFile file);

    // 2️⃣ Get Document By ID
    DocumentResponseDTO getDocumentById(UUID documentId);

    // 3️⃣ Get All Documents by Entity (Claim/Policy/User)
    List<DocumentResponseDTO> getDocumentsByEntity(UUID entityId, String entityType);

    // 4️⃣ Download Document URL by DocumentId
    String getDocumentDownloadUrl(UUID documentId);

    // 5️⃣ Update Document Status (Verify/Reject)
    DocumentResponseDTO updateDocumentStatus(UUID documentId, String status, String remarks);

    // 6️⃣ Delete Document (Soft Delete)
    void deleteDocument(UUID documentId);
}
