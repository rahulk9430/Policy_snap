package com.insurance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.insurance.dto.PolicyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "policies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID policyId;

    private String policyNumber; 

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type")
    private PolicyType policyType;

    private Double premiumAmount;
    private Double coverageAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer durationInYears;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status; 

    private String nomineeName;
    private String nomineeRelation;

    @ManyToOne
    @JoinColumn(name = "user_id") // Foreign key to User table
    private User user;

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
