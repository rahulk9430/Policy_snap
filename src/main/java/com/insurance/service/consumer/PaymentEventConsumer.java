package com.insurance.service.consumer;

import com.insurance.model.EventMessage;
import com.insurance.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PaymentEventConsumer {

    private final EmailService emailService;

    public PaymentEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "insurance.payment.events", groupId = "insurance-group")
    public void consumePaymentEvent(EventMessage event) {
        try {
            UUID userId = event.getUserId();
            String eventType = event.getEventType();
            Map<String, Object> data = event.getData();

            String subject = "Payment Event: " + eventType;
            String body = "Dear User,\n\n"
                    + "Your payment has been processed.\n"
                    + "Details:\n"
                    + "Amount: " + data.get("amount") + "\n"
                    + "Policy Number: " + data.get("policyNumber") + "\n"
                    + "Transaction ID: " + data.get("transactionId") + "\n"
                    + "Status: " + data.get("status") + "\n\n"
                    + "Regards,\nInsurance Team";

            // Send Email
            emailService.sendEmail(userId, subject, body);

            System.out.println("✅ Email sent for payment event: " + eventType + " to userId: " + userId);

        } catch (Exception e) {
            System.err.println("❌ Error processing payment event: " + e.getMessage());
        }
    }
}
