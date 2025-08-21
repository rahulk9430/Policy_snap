package com.insurance.dto.claims;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;


@Data
public class ClaimResponseDTO {

    private UUID claimId;

    private String claimNumber;

    private UUID policyId;

    private UUID userId;  // <-- Add userId in response too

    private ClaimStatus status;

    private ClaimType claimType;

    private Double claimedAmount;

    private String reasonForClaim;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
