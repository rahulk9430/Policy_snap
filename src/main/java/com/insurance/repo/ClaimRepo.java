package com.insurance.repo;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.dto.claims.ClaimStatus;
import com.insurance.model.Claim;

import jakarta.transaction.Transactional;

@Repository
public interface ClaimRepo extends JpaRepository<Claim, UUID> {

    // 1. Get All Claims By User ID
    List<Claim> findByUser_UserId(UUID userId);

    // 2. Get Claims By Status
    List<Claim> findByStatus(ClaimStatus status);

    // 3. Search Claim By Claim Number
Optional<Claim> findByClaimNumber(String claimNumber);

    // 4. Get All Claims of a Policy ID
    List<Claim> findByPolicy_PolicyId(UUID policyId);

    // In ClaimRepo.java
    List<Claim> findByPolicy_PolicyNumber(String policyNumber);


  @Modifying
    @Transactional
    @Query("UPDATE Claim c SET c.status = :status WHERE c.policy.policyNumber = :policyNumber")
    void updateClaimStatusByPolicyNumber(@Param("policyNumber") String policyNumber,
                                         @Param("status") ClaimStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Claim c SET c.status = :status WHERE c.user.userId = :userId")
    void updateClaimStatusByUserId(@Param("userId") Long userId,
                                   @Param("status") ClaimStatus status);

    @Modifying
    @Query("UPDATE Claim c SET c.status = :status WHERE c.claimId = :claimId")
    int updateStatusByClaimId(@Param("status") ClaimStatus status, @Param("claimId") UUID claimId);

    @Modifying
    @Query("UPDATE Claim c SET c.status = :status WHERE c.claimNumber = :claimNumber")
    int updateStatusByClaimNumber(@Param("status") ClaimStatus status, @Param("claimNumber") String claimNumber);

}
