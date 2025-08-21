package com.insurance.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.insurance.dto.claims.ClaimRequestDTO;
import com.insurance.dto.claims.ClaimResponseDTO;
import com.insurance.dto.claims.ClaimStatus;
import com.insurance.events.EventPublisher;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.helper.PublishClaimEvent;
import com.insurance.model.Claim;
import com.insurance.model.EventMessage;
import com.insurance.model.Policy;
import com.insurance.model.User;
import com.insurance.repo.ClaimRepo;
import com.insurance.repo.PolicyRepo;
import com.insurance.repo.UserRepo;
import com.insurance.service.ClaimService;
import com.insurance.util.GenericMapper;

import jakarta.transaction.Transactional;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepo claimRepo;
    private final PolicyRepo policyRepo;
    private final UserRepo userRepo;
    private final GenericMapper genericMapper;
    private final EventPublisher eventPublisher;
    private final PublishClaimEvent publishClaimEvent;

    public ClaimServiceImpl(ClaimRepo claimRepo, PolicyRepo policyRepo, UserRepo userRepo,
    GenericMapper genericMapper, EventPublisher eventPublisher, PublishClaimEvent publishClaimEvent) {
        this.claimRepo = claimRepo;
        this.policyRepo = policyRepo;
        this.userRepo = userRepo;
        this.genericMapper = genericMapper;
        this.eventPublisher = eventPublisher;
        this.publishClaimEvent = publishClaimEvent;
    }

    @Override
    public ClaimResponseDTO createClaim(ClaimRequestDTO requestDto) {
        Policy policy = policyRepo.findById(requestDto.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + requestDto.getPolicyId()));

        User user = userRepo.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + requestDto.getUserId()));

        Claim claim = genericMapper.mapToEntity(requestDto, Claim.class);
        claim.setClaimId(null);
        claim.setClaimNumber(generateClaimNumber());
        claim.setPolicy(policy);
        claim.setUser(user);
        claim.setStatus(ClaimStatus.PENDING);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());

        Claim savedClaim = claimRepo.save(claim);

        // ClaimService.java (on create/update status)
String ev = switch (claim.getStatus()) {
  case PENDING -> "CLAIM_SUBMITTED";
  case UNDER_REVIEW -> "CLAIM_UNDER_REVIEW";
  case APPROVED -> "CLAIM_APPROVED";
  case REJECTED -> "CLAIM_REJECTED";
    default -> throw new IllegalArgumentException("Unexpected value: " + claim.getStatus());
};
eventPublisher.publishAfterCommit("insurance.claim.events",
  EventMessage.builder()
    .eventId(UUID.randomUUID())
    .eventType(ev)
    .aggregateType("CLAIM")
    .aggregateId(claim.getClaimId())
    .userId(claim.getUser().getUserId())
    .occurredAt(LocalDateTime.now())
    .source("claim-service")
    .data(Map.of(
        "claimNumber", claim.getClaimNumber(),
        "claimType", claim.getClaimType().name(),
        "claimedAmount", claim.getClaimedAmount(),
        "remarks", claim.getRemarks()
    ))
    .build()
);


        return genericMapper.mapToDto(savedClaim, ClaimResponseDTO.class);
    }

    @Override
    public ClaimResponseDTO updateClaim(UUID claimId, ClaimRequestDTO requestDto) {
        Claim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        claim.setClaimedAmount(requestDto.getClaimedAmount());
        claim.setClaimType(requestDto.getClaimType());
        claim.setUpdatedAt(LocalDateTime.now());

        Claim updatedClaim = claimRepo.save(claim);

         String ev = switch (claim.getStatus()) {
        case PENDING -> "CLAIM_UPDATED_PENDING";
        case UNDER_REVIEW -> "CLAIM_UPDATED_UNDER_REVIEW";
        case APPROVED -> "CLAIM_UPDATED_APPROVED";
        case REJECTED -> "CLAIM_UPDATED_REJECTED";
        default -> "CLAIM_UPDATED";
    };

    eventPublisher.publishAfterCommit("insurance.claim.events",
        EventMessage.builder()
            .eventId(UUID.randomUUID())
            .eventType(ev)
            .aggregateType("CLAIM")
            .aggregateId(claim.getClaimId())
            .userId(claim.getUser().getUserId())
            .occurredAt(LocalDateTime.now())
            .source("claim-service")
            .data(Map.of(
                "claimNumber", claim.getClaimNumber(),
                "claimType", claim.getClaimType().name(),
                "claimedAmount", claim.getClaimedAmount(),
                "remarks", claim.getRemarks()
            ))
            .build()
    );

        return genericMapper.mapToDto(updatedClaim, ClaimResponseDTO.class);
    }

    @Override
    public ClaimResponseDTO getClaimById(UUID claimId) {
        Claim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));
        return genericMapper.mapToDto(claim, ClaimResponseDTO.class);
    }

    @Override
    public ClaimResponseDTO getClaimByClaimNumber(String claimNumber) {
        Claim claim = claimRepo.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with Claim Number: " + claimNumber));
        return genericMapper.mapToDto(claim, ClaimResponseDTO.class);
    }

    @Override
    public List<ClaimResponseDTO> getAllClaims() {
        return claimRepo.findAll().stream()
                .map(claim -> genericMapper.mapToDto(claim, ClaimResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimResponseDTO> getClaimsByUserId(UUID userId) {
        List<Claim> claims = claimRepo.findByUser_UserId(userId);
        return claims.stream()
                .map(claim -> genericMapper.mapToDto(claim, ClaimResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimResponseDTO> getClaimsByPolicyId(UUID policyId) {
        List<Claim> claims = claimRepo.findByPolicy_PolicyId(policyId);
        return claims.stream()
                .map(claim -> genericMapper.mapToDto(claim, ClaimResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimResponseDTO> getClaimsByStatus(String status) {
        ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());
        List<Claim> claims = claimRepo.findByStatus(claimStatus);
        return claims.stream()
                .map(claim -> genericMapper.mapToDto(claim, ClaimResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClaim(UUID claimId) {
        Claim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));
        claimRepo.delete(claim);
    }

    @Override
    public List<ClaimResponseDTO> getClaimsByPolicyNumber(String policyNumber) {
        List<Claim> claims = claimRepo.findByPolicy_PolicyNumber(policyNumber);
        return claims.stream()
                .map(claim -> genericMapper.mapToDto(claim, ClaimResponseDTO.class))
                .collect(Collectors.toList());
    }



@Transactional
@Override
public void updateClaimStatusByPolicyNumber(String policyNumber, ClaimStatus status) {
    claimRepo.updateClaimStatusByPolicyNumber(policyNumber, status);
    

    // ab updated claims fetch karo
    List<Claim> updatedClaims = claimRepo.findByPolicy_PolicyNumber(policyNumber);

    for (Claim claim : updatedClaims) {
publishClaimEvent.publishClaimEvent(claim);
    }
}

@Transactional
@Override
public void updateClaimStatusByClaimNumber(String claimNumber, String status) {
    ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());
    int updatedRows = claimRepo.updateStatusByClaimNumber(claimStatus, claimNumber);
    if (updatedRows == 0) {
        throw new ResourceNotFoundException("No claim found with claim number: " + claimNumber);
    }

    // ab updated claim fetch karo
    Claim claim = claimRepo.findByClaimNumber(claimNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Claim not found with Claim Number: " + claimNumber));

publishClaimEvent.publishClaimEvent(claim);
}



@Transactional
@Override
public void updateClaimStatusByClaimId(UUID claimId, String status) {
    ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());
    Claim claim = claimRepo.findById(claimId)
            .orElseThrow(() -> new ResourceNotFoundException("No claim found with ID: " + claimId));

    claim.setStatus(claimStatus);
    claim.setUpdatedAt(LocalDateTime.now());
    claimRepo.save(claim);

    // 🔹 Publish event for status change
    String ev = "CLAIM_STATUS_" + claimStatus.name();

    eventPublisher.publishAfterCommit("insurance.claim.events",
        EventMessage.builder()
            .eventId(UUID.randomUUID())
            .eventType(ev)
            .aggregateType("CLAIM")
            .aggregateId(claim.getClaimId())
            .userId(claim.getUser().getUserId())
            .occurredAt(LocalDateTime.now())
            .source("claim-service")
            .data(Map.of(
                "claimNumber", claim.getClaimNumber(),
                "status", claimStatus.name(),
                "remarks", claim.getRemarks()
            ))
            .build()
    );
}




    private String generateClaimNumber() {
        return "CLM" + System.currentTimeMillis();
    }

    @Override
    public void deleteClaimById(UUID claimId) {
        Claim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));
        claimRepo.delete(claim);
    }
}
