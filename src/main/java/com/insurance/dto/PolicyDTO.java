package com.insurance.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDTO {
    private UUID policyId;
    private String policyNumber;
    private PolicyType policyType;
    private Double premiumAmount;
    private Integer durationInYears;
    private String status;

    private String nomineeName;
    private String nomineeRelation;

    private UUID userId;
}
