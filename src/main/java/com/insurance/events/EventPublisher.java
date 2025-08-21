package com.insurance.events;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.insurance.model.EventMessage;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, EventMessage> kafkaTemplate;

    public void publishAfterCommit(String topic, EventMessage msg) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    kafkaTemplate.send(topic, msg.getAggregateId().toString(), msg);
                }
            });
        } else {
            kafkaTemplate.send(topic, msg.getAggregateId().toString(), msg);
        }
    }
}
