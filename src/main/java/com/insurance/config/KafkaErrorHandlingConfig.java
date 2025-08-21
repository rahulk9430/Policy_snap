package com.insurance.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.insurance.model.EventMessage;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaErrorHandlingConfig {
  private final KafkaTemplate<String, EventMessage> template;

  @Bean
  public DefaultErrorHandler errorHandler() {
    DeadLetterPublishingRecoverer recoverer =
      new DeadLetterPublishingRecoverer(template,
        (record, ex) -> new TopicPartition("insurance.notification.dlt", record.partition()));
    return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3)); // 3 retries
  }
}
