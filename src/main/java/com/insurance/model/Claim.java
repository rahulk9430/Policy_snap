package com.insurance.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.insurance.dto.claims.ClaimStatus;
import com.insurance.dto.claims.ClaimType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "claims")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID claimId;

    @Column(unique = true)
    private String claimNumber;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;   // <-- Ye field added hai (Kis user ne claim kiya hai)

    @Enumerated(EnumType.STRING)
    private ClaimStatus status; // PENDING, APPROVED, REJECTED, UNDER_REVIEW

    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
    
    private Double claimedAmount;

    private String reasonForClaim;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
