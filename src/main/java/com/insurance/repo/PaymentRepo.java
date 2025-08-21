package com.insurance.repo;

import com.insurance.model.Payment;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {

    Payment findByTransactionId(String transactionId);
} 
