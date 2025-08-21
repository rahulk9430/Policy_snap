package com.insurance.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.dto.Document.DocumentRequestDTO;
import com.insurance.dto.Document.DocumentResponseDTO;
import com.insurance.model.ApiResponse;
import com.insurance.service.DocumentService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

 @PostMapping
public ResponseEntity<ApiResponse<DocumentResponseDTO>> uploadDocument(
    @RequestPart("metadata") String metadataJson,
    @RequestPart("file") MultipartFile file) throws IOException {
        
    ObjectMapper mapper = new ObjectMapper();
    DocumentRequestDTO requestDto = mapper.readValue(metadataJson, DocumentRequestDTO.class);

    DocumentResponseDTO responseDto = documentService.uploadDocument(requestDto, file);

    return ResponseEntity.ok(new ApiResponse<>("Document uploaded successfully", responseDto, true));
}



    // 2️⃣ Get Document by ID
    @GetMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocumentResponseDTO>> getDocumentById(@PathVariable UUID documentId) {
        DocumentResponseDTO dto = documentService.getDocumentById(documentId);
        return ResponseEntity.ok(new ApiResponse<>("Document fetched successfully", dto, true));
    }

    // 3️⃣ Get Documents by Entity (Policy/Claim/User)
    @GetMapping("/entity")
    public ResponseEntity<ApiResponse<List<DocumentResponseDTO>>> getDocumentsByEntity(
            @RequestParam UUID entityId,
            @RequestParam String entityType) {
        
        List<DocumentResponseDTO> list = documentService.getDocumentsByEntity(entityId, entityType);
        return ResponseEntity.ok(new ApiResponse<>("Documents fetched successfully", list, true));
    }

    // 4️⃣ Update Document Status (VERIFY/REJECT)
    @PutMapping("/{documentId}/status")
    public ResponseEntity<ApiResponse<DocumentResponseDTO>> updateDocumentStatus(
            @PathVariable UUID documentId,
            @RequestParam String status,
            @RequestParam(required = false) String remarks) {
        
        DocumentResponseDTO updated = documentService.updateDocumentStatus(documentId, status, remarks);
        return ResponseEntity.ok(new ApiResponse<>("Document status updated", updated, true));
    }

    // 5️⃣ Delete Document (Soft Delete)
    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable UUID documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok(new ApiResponse<>("Document deleted successfully", null, true));
    }

    // 6️⃣ Get Document Download URL
    @GetMapping("/{documentId}/download-url")
    public ResponseEntity<ApiResponse<String>> getDownloadUrl(@PathVariable UUID documentId) {
        String url = documentService.getDocumentDownloadUrl(documentId);
        return ResponseEntity.ok(new ApiResponse<>("Download URL fetched successfully", url, true));
    }
}
