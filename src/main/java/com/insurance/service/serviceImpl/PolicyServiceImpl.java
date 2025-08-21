package com.insurance.service.serviceImpl;

import com.insurance.repo.PolicyRepo;
import com.insurance.repo.UserRepo;
import com.insurance.util.GenericMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.insurance.dto.PolicyDTO;
import com.insurance.dto.Policy.PolicyCreateRequestDTO;
import com.insurance.dto.Policy.PolicyResponseDTO;
import com.insurance.events.EventPublisher;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.PolicyStatus;
import com.insurance.model.EventMessage;
import com.insurance.model.Policy;
import com.insurance.model.User;
import com.insurance.service.PolicyService;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepo policyRepo;
    private final UserRepo userRepo;
    private final GenericMapper genericMapper;
    private final EventPublisher eventPublisher;

    public PolicyServiceImpl(PolicyRepo policyRepo, UserRepo userRepo, GenericMapper genericMapper,
                             EventPublisher eventPublisher) {
        this.policyRepo = policyRepo;
        this.userRepo = userRepo;
        this.genericMapper = genericMapper;
        this.eventPublisher = eventPublisher;
    }

   @Override
public PolicyResponseDTO createPolicy(PolicyCreateRequestDTO dto) {
    Policy policy = genericMapper.mapToEntity(dto, Policy.class);
    policy.setPolicyId(null);


    User user = userRepo.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));

    policy.setUser(user);
    policy.setPolicyNumber(generatePolicyNumber());
    policy.setStatus(PolicyStatus.ACTIVE);

    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusYears(dto.getDurationInYears());

    policy.setStartDate(startDate);
    policy.setEndDate(endDate);

    // Coverage Calculation Logic
    Double coverageAmount = dto.getPremiumAmount() * 100;  // Example Formula
    policy.setCoverageAmount(coverageAmount);

    Policy saved = policyRepo.save(policy);

    // PolicyService.java (on policy create)
eventPublisher.publishAfterCommit("insurance.policy.events",
  EventMessage.builder()
    .eventId(UUID.randomUUID())
    .eventType("POLICY_CREATED")
    .aggregateType("POLICY")
    .aggregateId(policy.getPolicyId())
    .userId(policy.getUser().getUserId())
    .occurredAt(LocalDateTime.now())
    .source("policy-service")
    .data(Map.of(
        "policyNumber", policy.getPolicyNumber(),
        "policyType", policy.getPolicyType().name(),
        "startDate", policy.getStartDate().toString(),
        "endDate", policy.getEndDate().toString()
    ))
    .build()
);

    return genericMapper.mapToDto(saved, PolicyResponseDTO.class);
}



    @Override
    public List<PolicyResponseDTO> getAllPolicies() {
        return policyRepo.findAll().stream()
                .map(p -> genericMapper.mapToDto(p, PolicyResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PolicyResponseDTO getPolicyById(UUID policyId) {
        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + policyId));
        return genericMapper.mapToDto(policy, PolicyResponseDTO.class);
    }

    @Override
    public List<PolicyResponseDTO> getPoliciesByUserId(UUID userId) {
        List<Policy> policies = policyRepo.findByUser_UserId(userId);
        return policies.stream()
                .map(p -> genericMapper.mapToDto(p, PolicyResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PolicyResponseDTO updatePolicy(UUID policyId, PolicyDTO dto) {
        Policy existing = policyRepo.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + policyId));

        
        existing.setNomineeName(dto.getNomineeName());
        existing.setNomineeRelation(dto.getNomineeRelation());

        Policy updated = policyRepo.save(existing);
        return genericMapper.mapToDto(updated, PolicyResponseDTO.class);
    }

    @Override
    public void deletePolicy(UUID policyId) {
        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + policyId));
        policyRepo.delete(policy);
    }

    @Override
    public PolicyDTO changePolicyStatus(UUID policyId, String newStatus) {
        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + policyId));
        policy.setStatus(PolicyStatus.valueOf(newStatus.toUpperCase()));
        return genericMapper.mapToDto(policyRepo.save(policy), PolicyDTO.class);
    }

    @Override
    public List<PolicyResponseDTO> getActivePolicies() {
        return policyRepo.findByStatus(PolicyStatus.ACTIVE).stream()
                .map(p -> genericMapper.mapToDto(p, PolicyResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public PolicyResponseDTO getPolicyByNumber(String policyNumber) {
        Policy policy = policyRepo.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with number: " + policyNumber));
        return genericMapper.mapToDto(policy, PolicyResponseDTO.class);
    }

   


    private String generatePolicyNumber() {
        return "POL" + System.currentTimeMillis();
    }

   


}