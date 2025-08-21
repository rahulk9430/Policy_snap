package com.insurance.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.insurance.dto.Payment.PaymentDTO;
import com.insurance.dto.Payment.PaymentStatus;
import com.insurance.events.EventPublisher;
import com.insurance.model.EventMessage;
import com.insurance.model.Payment;
import com.insurance.model.Policy;
import com.insurance.model.User;
import com.insurance.repo.PaymentRepo;
import com.insurance.repo.PolicyRepo;
import com.insurance.repo.UserRepo;
import com.insurance.service.PaymentService;
import com.insurance.util.GenericMapper;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepository;
    private final UserRepo userRepository;
    private final PolicyRepo policyRepository;
    private final GenericMapper genericMapper;
    private final EventPublisher eventPublisher;

    public PaymentServiceImpl(PaymentRepo paymentRepository, UserRepo userRepository, PolicyRepo policyRepository,
            GenericMapper genericMapper, EventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.genericMapper = genericMapper;
        this.eventPublisher = eventPublisher;
    }

    public PaymentDTO makePayment(UUID userId, UUID policyId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPolicy(policy);
        payment.setAmount(amount);
        payment.setTransactionId("TXN-" + UUID.randomUUID());

        // Mock success/failure
        boolean success = new Random().nextBoolean();
        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        Payment saved = paymentRepository.save(payment);

        // PaymentService.java (after payment save/update)
        eventPublisher.publishAfterCommit("insurance.payment.events",
                EventMessage.builder()
                        .eventId(UUID.randomUUID())
                        .eventType(payment.getStatus().name().equals("SUCCESS") ? "PAYMENT_SUCCESS" : "PAYMENT_FAILED")
                        .aggregateType("PAYMENT")
                        .aggregateId(payment.getPaymentId())
                        .userId(payment.getUser().getUserId())
                        .occurredAt(LocalDateTime.now())
                        .source("payment-service")
                        .data(Map.of(
                                "amount", payment.getAmount(),
                                "policyNumber", payment.getPolicy().getPolicyNumber(),
                                "transactionId", payment.getTransactionId(),
                                "status", payment.getStatus().name()))
                        .build());

        // Convert to DTO
        PaymentDTO dto = genericMapper.mapToDto(saved, PaymentDTO.class);
        dto.setUserId(user.getUserId());
        dto.setPolicyId(policy.getPolicyId());

        return dto;
    }

    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment == null) {
            throw new RuntimeException("Payment not found for transaction ID: " + transactionId);
        }
        return genericMapper.mapToDto(payment, PaymentDTO.class);
    }

}
