package com.insurance.Scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.insurance.events.EventPublisher;
import com.insurance.model.EventMessage;
import com.insurance.model.Policy;
import com.insurance.repo.PolicyRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PremiumReminderScheduler {
  private final PolicyRepo policyRepo;
  private final EventPublisher eventPublisher;

  // Daily 9 AM
  @Scheduled(cron = "0 0 9 * * *")
  public void publishRenewalReminders() {
    LocalDate target = LocalDate.now().plusDays(15);
    List<Policy> dueSoon = policyRepo.findByEndDateBetween(LocalDate.now().plusDays(14), LocalDate.now().plusDays(16));
    for (Policy p : dueSoon) {
      eventPublisher.publishAfterCommit("insurance.policy.events",
        EventMessage.builder()
          .eventId(UUID.randomUUID())
          .eventType("POLICY_RENEWAL_DUE")
          .aggregateType("POLICY")
          .aggregateId(p.getPolicyId())
          .userId(p.getUser().getUserId())
          .occurredAt(LocalDateTime.now())
          .source("policy-service")
          .data(Map.of("policyNumber", p.getPolicyNumber(), "endDate", p.getEndDate().toString()))
          .build()
      );
    }
  }
}


