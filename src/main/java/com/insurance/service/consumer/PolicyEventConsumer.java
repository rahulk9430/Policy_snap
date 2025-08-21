package com.insurance.service.consumer;


import com.insurance.model.EventMessage;
import com.insurance.service.EmailService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PolicyEventConsumer {

    private final EmailService emailService;

    public PolicyEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "insurance.policy.events", groupId = "notification-group")
    public void consumePolicyEvents(EventMessage event) {
        if ("POLICY_CREATED".equals(event.getEventType())) {
            // Policy create hone par email bhejna
            String emailBody = "Dear User,\n\nYour Policy has been created successfully.\n"
                    + "Policy Number: " + event.getData().get("policyNumber") + "\n"
                    + "Policy Type: " + event.getData().get("policyType") + "\n"
                    + "Start Date: " + event.getData().get("startDate") + "\n"
                    + "End Date: " + event.getData().get("endDate") + "\n\n"
                    + "Thanks,\nInsurance Team";

            // user ki email userId ke base pe DB se nikalna hoga
            emailService.sendEmail(event.getUserId(), "Policy Created Successfully", emailBody);
        }
    }
}
