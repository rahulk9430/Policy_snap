package com.insurance.dto.Document;

import java.util.UUID;

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
public class DocumentRequestDTO {

    private DocumentType documentType;
    private EntityType entityType;
    private UUID entityId;
    private UUID uploadedByUserId; 
    
}
