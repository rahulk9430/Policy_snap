package com.insurance.dto.claims;

import java.util.UUID;

import lombok.Data;

@Data
public class ClaimRequestDTO {

    private UUID policyId;
    private UUID claimId;
    private UUID userId; // <-- User Id of who is creating the claim

    private Double claimedAmount;

    private String reasonForClaim;

    private ClaimType claimType;

    private String remarks;
}
