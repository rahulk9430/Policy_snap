package com.insurance.dto.Payment;


import lombok.Data;
import com.insurance.dto.Payment.PaymentStatus;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private UUID paymentId;
    private UUID userId;
    private UUID policyId;
    private Double amount;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
