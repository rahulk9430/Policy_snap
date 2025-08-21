package com.insurance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaConfig {


    // KafkaConfig.java
@Bean
public NewTopic userTopic()     { return TopicBuilder.name("insurance.user.events").partitions(3).replicas(1).build(); }
@Bean
public NewTopic policyTopic()   { return TopicBuilder.name("insurance.policy.events").partitions(3).replicas(1).build(); }
@Bean
public NewTopic paymentTopic()  { return TopicBuilder.name("insurance.payment.events").partitions(3).replicas(1).build(); }
@Bean
public NewTopic claimTopic()    { return TopicBuilder.name("insurance.claim.events").partitions(3).replicas(1).build(); }
@Bean
public NewTopic documentTopic() { return TopicBuilder.name("insurance.document.events").partitions(3).replicas(1).build(); }
@Bean
public NewTopic dltTopic()      { return TopicBuilder.name("insurance.notification.dlt").partitions(3).replicas(1).build(); }

    
}
