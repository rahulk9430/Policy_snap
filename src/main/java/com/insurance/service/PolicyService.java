package com.insurance.service;

import java.util.List;
import java.util.UUID;

import com.insurance.dto.PolicyDTO;
import com.insurance.dto.Policy.PolicyCreateRequestDTO;
import com.insurance.dto.Policy.PolicyResponseDTO;

public interface PolicyService {

    // 1. Create Policy for a Customer (Auto-generate policy number)
    PolicyResponseDTO createPolicy(PolicyCreateRequestDTO policyDto);

    // 2. Get all policies
    List<PolicyResponseDTO> getAllPolicies();

    // 3. Get policy by ID
    PolicyResponseDTO getPolicyById(UUID policyId);

    // 4. Get all policies of a specific user
    List<PolicyResponseDTO> getPoliciesByUserId(UUID userId);

    // 5. Update existing policy
  PolicyResponseDTO updatePolicy(UUID policyId, PolicyDTO policyDto);

    // 6. Delete (soft delete or hard delete)
    void deletePolicy(UUID policyId);

    // 7. Change status of policy (e.g. ACTIVE → CANCELLED / EXPIRED)
    PolicyDTO changePolicyStatus(UUID policyId, String newStatus);

    // 8. Get active policies (for dashboard/validity check)
    List<PolicyResponseDTO> getActivePolicies();

    

    // 10. Search policy by policy number
    PolicyResponseDTO getPolicyByNumber(String policyNumber);

   

   
}

