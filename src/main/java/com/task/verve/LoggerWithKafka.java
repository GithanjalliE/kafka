package com.task.verve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoggerWithKafka {

    private static final Logger logger = LoggerFactory.getLogger(UniqueRequestLogger.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AtomicInteger uniqueCount;

    @Autowired
    public LoggerWithKafka(KafkaTemplate<String, String> kafkaTemplate, AtomicInteger uniqueCount) {
        this.kafkaTemplate = kafkaTemplate;
        this.uniqueCount = uniqueCount;
    }

    @Scheduled(fixedRate = 60000)
    public void logUniqueRequestCount() {
        int count = uniqueCount.getAndSet(0);
        String message = "Unique request count in the last minute: " + count;
        kafkaTemplate.send("unique-requests-topic", message); // Send to Kafka
        logger.info(message);
    }
}

