package com.insurance.dto.Policy;

import java.time.LocalDateTime;
import java.util.UUID;

import com.insurance.dto.PolicyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponseDTO {
    private UUID policyId;
    private String policyNumber;
    private PolicyType policyType;
    private Double premiumAmount;
    private Double coverageAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String nomineeName;
    private String nomineeRelation;
    private UUID userId;
}
