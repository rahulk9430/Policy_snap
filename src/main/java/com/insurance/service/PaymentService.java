package com.insurance.service;

import java.util.UUID;

import com.insurance.dto.Payment.PaymentDTO;

public interface PaymentService {

    PaymentDTO makePayment(UUID userId, UUID policyId, Double amount);
    // PaymentDTO getPaymentById(UUID paymentId);
    // List<PaymentDTO> getAllPayments();
    // PaymentDTO updatePayment(UUID paymentId, PaymentDTO paymentDTO);
    // void deletePayment(UUID paymentId);

    PaymentDTO getPaymentByTransactionId(String transactionId);
}
