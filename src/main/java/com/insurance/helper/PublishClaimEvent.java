package com.insurance.helper;   

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.insurance.events.EventPublisher;
import com.insurance.model.Claim;
import com.insurance.model.EventMessage;

@Component
public class PublishClaimEvent {

    private final EventPublisher eventPublisher;

    public PublishClaimEvent(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    // ✅ method public & camelCase
    public void publishClaimEvent(Claim claim) {
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
    }
}
