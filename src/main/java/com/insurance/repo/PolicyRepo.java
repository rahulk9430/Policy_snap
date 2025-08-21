package com.insurance.repo;

import com.insurance.model.Policy;
import com.insurance.model.PolicyStatus;
import com.insurance.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PolicyRepo extends JpaRepository<Policy, UUID> {

    List<Policy> findByUser_UserId(UUID userId);

    Optional<Policy> findByPolicyNumber(String policyNumber);

    Optional<Policy> findByUser(User user);

    List<Policy>   findByStatus(PolicyStatus status);

    List<Policy> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

}
