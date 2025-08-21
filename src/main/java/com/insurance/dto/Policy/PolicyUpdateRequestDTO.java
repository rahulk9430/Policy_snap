package com.insurance.dto.Policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyUpdateRequestDTO {
     private String nomineeName;
    private String nomineeRelation;
}
