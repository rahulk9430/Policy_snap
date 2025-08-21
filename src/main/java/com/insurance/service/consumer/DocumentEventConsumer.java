package com.insurance.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DocumentEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public DocumentEventConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "insurance.document.events", groupId = "insurance-group")
    public void consumeDocumentEvent(String message) {
        try {
            // Parse JSON string into a Map
            Map<String, Object> event = objectMapper.readValue(message, Map.class);

            // Extract userId, subject, body
            String userIdStr = (String) event.get("userId");
            UUID userId = UUID.fromString(userIdStr);

            String eventType = (String) event.get("eventType");
            Map<String, Object> data = (Map<String, Object>) event.get("data");

            String subject = "Document Event: " + eventType;
            String body = "Dear User,\n\n"
                    + "A document event occurred in the system.\n"
                    + "Details:\n"
                    + "File Name: " + data.get("fileName") + "\n"
                    + "Document Type: " + data.get("documentType") + "\n"
                    + "Entity Type: " + data.get("entityType") + "\n\n"
                    + "Regards,\nInsurance Team";

            // Send Email
            emailService.sendEmail(userId, subject, body);

            System.out.println("✅ Email sent for document event: " + eventType + " to userId: " + userId);

        } catch (Exception e) {
            System.err.println("❌ Error processing document event: " + e.getMessage());
        }
    }
}
