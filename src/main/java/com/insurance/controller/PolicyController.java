package com.insurance.controller;

import com.insurance.dto.PolicyDTO;
import com.insurance.dto.Policy.PolicyCreateRequestDTO;
import com.insurance.dto.Policy.PolicyResponseDTO;
import com.insurance.model.ApiResponse;
import com.insurance.service.PolicyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 1. Create Policy
    @PostMapping
    public ResponseEntity<ApiResponse<PolicyResponseDTO>> createPolicy(@RequestBody PolicyCreateRequestDTO policyDto) {
        PolicyResponseDTO created = policyService.createPolicy(policyDto);
        return ResponseEntity.ok(new ApiResponse<>("Policy created successfully", created,true));
    }

    // 2. Get all policies
    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyResponseDTO>>> getAllPolicies() {
        List<PolicyResponseDTO> list = policyService.getAllPolicies();
        return ResponseEntity.ok(new ApiResponse<>("Fetched all policies", list,true));
    }

    // 3. Get policy by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponseDTO>> getPolicyById(@PathVariable UUID id) {
        PolicyResponseDTO dto = policyService.getPolicyById(id);
        return ResponseEntity.ok(new ApiResponse<>("Fetched policy by ID", dto,true));
    }

    // 4. Get all policies by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PolicyResponseDTO>>> getPoliciesByUserId(@PathVariable UUID userId) {
        List<PolicyResponseDTO> list = policyService.getPoliciesByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>("Fetched user policies", list,true));
    }

   // 5. Update policy
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponseDTO>> updatePolicy(@PathVariable UUID id, @RequestBody PolicyDTO dto) {
        PolicyResponseDTO updated = policyService.updatePolicy(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Policy updated successfully", updated,true));
    }

    // 6. Delete policy
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<List<PolicyResponseDTO>>> deletePolicy(@PathVariable UUID id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok(new ApiResponse<>("Policy deleted successfully", null,true));
    }

    // 7. Change status of policy
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PolicyDTO>> changeStatus(@PathVariable UUID id, @RequestParam String status) {
        PolicyDTO updated = policyService.changePolicyStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>("Policy status updated", updated,true));
    }

    // 8. Get active policies
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PolicyResponseDTO>>> getActivePolicies() {
        List<PolicyResponseDTO> list = policyService.getActivePolicies();
        return ResponseEntity.ok(new ApiResponse<>("Fetched active policies", list,true));
    }

    // 9. Get policy by policy number
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PolicyResponseDTO>> getPolicyByNumber(@RequestParam String number) {
        PolicyResponseDTO dto = policyService.getPolicyByNumber(number);
        return ResponseEntity.ok(new ApiResponse<>("Fetched policy by number", dto,true));
    }
}
