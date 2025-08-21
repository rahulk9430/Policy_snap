package com.insurance.dto.Policy;

import java.util.UUID;

import com.insurance.dto.PolicyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyCreateRequestDTO {
    private PolicyType policyType;
    private Double premiumAmount;
    private Integer durationInYears;
    private String nomineeName;
    private String nomineeRelation;
    private UUID userId;
}
