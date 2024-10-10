package com.task.verve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoggerWithKafka {


    private static final Logger logger = LoggerFactory.getLogger(LoggerWithKafka.class);
    private KafkaTemplate<String, String> kafkaTemplate;
    private AtomicInteger uniqueCount;
    private final AtomicCounterService counterService;

    @Autowired
    public LoggerWithKafka(KafkaTemplate<String, String> kafkaTemplate, AtomicCounterService counterService) {
        this.kafkaTemplate = kafkaTemplate;
        this.counterService = counterService;
    }

    @Scheduled(fixedRate = 60000)
    public void logUniqueRequestCount() {
        int count = uniqueCount.getAndSet(0);
        String message = "Unique request count in the last minute: " + count;
        kafkaTemplate.send("unique-requests-topic", message); // Send to Kafka
        logger.info(message);
    }
}

