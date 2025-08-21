package com.insurance.service;

import java.util.List;
import java.util.UUID;

import com.insurance.dto.claims.ClaimRequestDTO;
import com.insurance.dto.claims.ClaimResponseDTO;
import com.insurance.dto.claims.ClaimStatus;



public interface ClaimService {

    ClaimResponseDTO createClaim(ClaimRequestDTO requestDto);

    ClaimResponseDTO updateClaim(UUID claimId, ClaimRequestDTO requestDto);

    ClaimResponseDTO getClaimById(UUID claimId);

    ClaimResponseDTO getClaimByClaimNumber(String claimNumber);

    List<ClaimResponseDTO> getAllClaims();

    List<ClaimResponseDTO> getClaimsByUserId(UUID userId);

    List<ClaimResponseDTO> getClaimsByPolicyId(UUID policyId);

    List<ClaimResponseDTO> getClaimsByStatus(String status);

    void deleteClaim(UUID claimId);

    List<ClaimResponseDTO> getClaimsByPolicyNumber(String policyNumber);


    void updateClaimStatusByPolicyNumber(String policyNumber, ClaimStatus status);

    void updateClaimStatusByClaimId(UUID claimId, String status);

    void updateClaimStatusByClaimNumber(String claimNumber, String status);

    void deleteClaimById(UUID claimId);

}
