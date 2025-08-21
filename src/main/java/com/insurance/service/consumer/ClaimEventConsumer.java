package com.insurance.service.consumer;

import com.insurance.model.EventMessage;
import com.insurance.model.User;
import com.insurance.repo.UserRepo;
import com.insurance.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClaimEventConsumer {

    private final EmailService emailService;
    private final UserRepo userRepo;

    public ClaimEventConsumer(EmailService emailService, UserRepo userRepo) {
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    @KafkaListener(topics = "insurance.claim.events", groupId = "insurance-group")
    public void consumeClaimEvent(EventMessage eventMessage) {
        System.out.println("Received claim event: " + eventMessage);

        UUID userId = eventMessage.getUserId();
        User user = userRepo.findById(userId).orElse(null);

        if (user != null && user.getEmail() != null) {
            String subject = "Claim Update Notification";
            String body = "Your claim event occurred: " + eventMessage.getEventType()
                        + "\nClaim Number: " + eventMessage.getData().get("claimNumber")
                        + "\nClaim Type: " + eventMessage.getData().get("claimType")
                        + "\nClaimed Amount: " + eventMessage.getData().get("claimedAmount")
                        + "\nRemarks: " + eventMessage.getData().get("remarks");

            emailService.sendEmail(user.getUserId(), subject, body);
        } else {
            System.err.println("No email found for user: " + userId);
        }
    }
}
