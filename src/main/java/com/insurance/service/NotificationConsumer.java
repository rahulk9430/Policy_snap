package com.insurance.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.insurance.model.EventMessage;
import com.insurance.model.Notification;
import com.insurance.model.NotificationPreference;
import com.insurance.model.ProcessedEvent;
import com.insurance.repo.NotificationPreferenceRepo;
import com.insurance.repo.ProcessedEventRepo;
import com.insurance.util.TemplateRouter;

import lombok.RequiredArgsConstructor;

// NotificationConsumer.java
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
  private final TemplateRouter router;
  private final TemplateService templateService;
  private final NotificationPreferenceRepo prefRepo;
  private final EmailDispatcher emailDispatcher;
  private final SmsDispatcher smsDispatcher;
  private final InAppDispatcher inAppDispatcher;
  private final ProcessedEventRepo processedRepo;

  @KafkaListener(topics = {
      "insurance.user.events",
      "insurance.policy.events",
      "insurance.payment.events",
      "insurance.claim.events",
      "insurance.document.events"
  }, containerFactory = "kafkaListenerContainerFactory")
  public void onMessage(EventMessage e) throws Exception {
    if (processedRepo.existsById(e.getEventId())) return; // idempotent

    var templates = router.resolveTemplates(e);
    var prefs = prefRepo.findById(e.getUserId()).orElseGet(() -> {
      var p = new NotificationPreference(); p.setUserId(e.getUserId()); return p; // defaults true
    });

    Map<String, Object> model = new HashMap<>(e.getData());
    model.put("userId", e.getUserId());
    model.put("eventType", e.getEventType());
    model.put("occurredAt", e.getOccurredAt());

    for (String code : templates) {
      var ch = templateService.channel(code);
      switch (ch) {
        case EMAIL -> {
          if (!prefs.isEmailEnabled()) break;
          String body = templateService.render(code, model);
          String subject = templateService.subject(code);
          emailDispatcher.send(prefs.getEmail(), subject, body);
          inAppDispatcher.persist(buildNotification(e.getUserId(), ch, subject, body, Notification.Status.SENT));
        }
        case SMS -> {
          if (!prefs.isSmsEnabled()) break;
          String text = templateService.render(code, model);
          smsDispatcher.send(prefs.getPhone(), text);
          inAppDispatcher.persist(buildNotification(e.getUserId(), ch, "SMS", text, Notification.Status.SENT));
        }
        case IN_APP -> {
          if (!prefs.isInAppEnabled()) break;
          String text = templateService.render(code, model);
          inAppDispatcher.persist(buildNotification(e.getUserId(), ch, "Notification", text, Notification.Status.SENT));
        }
      }
    }

    processedRepo.save(new ProcessedEvent(){{
      setEventId(e.getEventId());
    }});
  }

  private Notification buildNotification(UUID userId, Notification.Channel ch, String title, String msg, Notification.Status st) {
    Notification n = new Notification();
    n.setUserId(userId); n.setChannel(ch); n.setTitle(title); n.setMessage(msg);
    n.setStatus(st); n.setSentAt(LocalDateTime.now());
    return n;
  }
}
