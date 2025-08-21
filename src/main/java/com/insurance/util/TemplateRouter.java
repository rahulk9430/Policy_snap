package com.insurance.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.insurance.model.EventMessage;

@Component
public class TemplateRouter {
  public List<String> resolveTemplates(EventMessage e) {
    return switch (e.getEventType()) {
      case "USER_REGISTERED"   -> List.of("USER_REGISTERED_EMAIL", "USER_REGISTERED_INAPP");
      case "PAYMENT_SUCCESS"   -> List.of("PAYMENT_SUCCESS_EMAIL", "PAYMENT_SUCCESS_INAPP", "PAYMENT_SUCCESS_SMS");
      case "PAYMENT_FAILED"    -> List.of("PAYMENT_FAILED_EMAIL", "PAYMENT_FAILED_INAPP", "PAYMENT_FAILED_SMS");
      case "CLAIM_SUBMITTED"   -> List.of("CLAIM_SUBMITTED_EMAIL", "CLAIM_SUBMITTED_INAPP");
      case "CLAIM_APPROVED"    -> List.of("CLAIM_APPROVED_EMAIL", "CLAIM_APPROVED_SMS");
      case "CLAIM_REJECTED"    -> List.of("CLAIM_REJECTED_EMAIL", "CLAIM_REJECTED_INAPP");
      case "DOCUMENT_VERIFIED" -> List.of("DOCUMENT_VERIFIED_EMAIL", "DOCUMENT_VERIFIED_INAPP");
      case "DOCUMENT_REJECTED" -> List.of("DOCUMENT_REJECTED_EMAIL", "DOCUMENT_REJECTED_INAPP");
      case "POLICY_CREATED"    -> List.of("POLICY_CREATED_EMAIL", "POLICY_CREATED_INAPP");
      case "POLICY_RENEWAL_DUE"-> List.of("POLICY_RENEWAL_DUE_EMAIL", "POLICY_RENEWAL_DUE_SMS", "POLICY_RENEWAL_DUE_INAPP");
      default -> List.of("GENERIC_INAPP");
    };
  }
}
