package com.insurance.controller;

import com.insurance.dto.claims.ClaimRequestDTO;
import com.insurance.dto.claims.ClaimResponseDTO;
import com.insurance.dto.claims.ClaimStatus;
import com.insurance.model.ApiResponse;
import com.insurance.service.ClaimService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // 1. Create Claim
    @PostMapping
    public ResponseEntity<ApiResponse<ClaimResponseDTO>> createClaim(@RequestBody ClaimRequestDTO requestDto) {
        ClaimResponseDTO created = claimService.createClaim(requestDto);
        return ResponseEntity.ok(new ApiResponse<>("Claim created successfully", created, true));
    }

    // 2. Update Claim
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClaimResponseDTO>> updateClaim(@PathVariable UUID id, @RequestBody ClaimRequestDTO requestDto) {
        ClaimResponseDTO updated = claimService.updateClaim(id, requestDto);
        return ResponseEntity.ok(new ApiResponse<>("Claim updated successfully", updated, true));
    }

    // 3. Get Claim by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClaimResponseDTO>> getClaimById(@PathVariable UUID id) {
        ClaimResponseDTO dto = claimService.getClaimById(id);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claim by ID", dto, true));
    }

    // 4. Get Claim by Claim Number
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ClaimResponseDTO>> getClaimByClaimNumber(@RequestParam String claimNumber) {
        ClaimResponseDTO dto = claimService.getClaimByClaimNumber(claimNumber);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claim by Claim Number", dto, true));
    }

    // 5. Get All Claims
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClaimResponseDTO>>> getAllClaims() {
        List<ClaimResponseDTO> list = claimService.getAllClaims();
        return ResponseEntity.ok(new ApiResponse<>("Fetched all claims", list, true));
    }

    // 6. Get Claims by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ClaimResponseDTO>>> getClaimsByUserId(@PathVariable UUID userId) {
        List<ClaimResponseDTO> list = claimService.getClaimsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claims by User ID", list, true));
    }

    // 7. Get Claims by Policy ID
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<ClaimResponseDTO>>> getClaimsByPolicyId(@PathVariable UUID policyId) {
        List<ClaimResponseDTO> list = claimService.getClaimsByPolicyId(policyId);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claims by Policy ID", list, true));
    }

    // 8. Get Claims by Policy Number
    @GetMapping("/policy/search")
    public ResponseEntity<ApiResponse<List<ClaimResponseDTO>>> getClaimsByPolicyNumber(@RequestParam String policyNumber) {
        List<ClaimResponseDTO> list = claimService.getClaimsByPolicyNumber(policyNumber);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claims by Policy Number", list, true));
    }

    // 9. Get Claims by Status
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<ClaimResponseDTO>>> getClaimsByStatus(@RequestParam String status) {
        List<ClaimResponseDTO> list = claimService.getClaimsByStatus(status);
        return ResponseEntity.ok(new ApiResponse<>("Fetched claims by status", list, true));
    }

    


    @PutMapping("/status/policy")
    public ResponseEntity<ApiResponse<Void>> updateClaimStatusByPolicyNumber(
        @RequestParam String policyNumber,
        @RequestParam String status) {
                ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());

    claimService.updateClaimStatusByPolicyNumber(policyNumber, claimStatus);
    return ResponseEntity.ok(new ApiResponse<>("Claim status updated successfully by Policy Number", null, true));
}

@PutMapping("/status/id/{claimId}")
public ResponseEntity<ApiResponse<Void>> updateClaimStatusByClaimId(
        @PathVariable UUID claimId,
        @RequestParam String status) {
    claimService.updateClaimStatusByClaimId(claimId, status);
    return ResponseEntity.ok(new ApiResponse<>("Claim status updated successfully by Claim ID", null, true));
}

@PutMapping("/status/number")
public ResponseEntity<ApiResponse<Void>> updateClaimStatusByClaimNumber(
        @RequestParam String claimNumber,
        @RequestParam String status) {
    claimService.updateClaimStatusByClaimNumber(claimNumber, status);
    return ResponseEntity.ok(new ApiResponse<>("Claim status updated successfully by Claim Number", null, true));
}

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(@PathVariable UUID id) {
        claimService.deleteClaimById(id);
        return ResponseEntity.ok(new ApiResponse<>("Claim deleted successfully", null, true));
    }

}
