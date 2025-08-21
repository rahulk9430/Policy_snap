package com.insurance.model;


import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor 
@NoArgsConstructor
public class EventMessage {
    private UUID eventId;           // for idempotency
    private String eventType;       // e.g. PAYMENT_SUCCESS
    private String aggregateType;   // USER/POLICY/CLAIM/DOCUMENT/PAYMENT
    private UUID aggregateId;       // policyId/claimId/paymentId...
    private UUID userId;            // jisko notify karna hai (primary)
    private LocalDateTime occurredAt;
    private String source;          // e.g. "policy-service"
    private String correlationId;   // for tracing
    private Map<String, Object> data; // small, necessary fields only
}

