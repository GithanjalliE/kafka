package com.task.verve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UniqueRequestLogger {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final AtomicInteger uniqueCount = new AtomicInteger(0); // For counting unique requests

    public void incrementUniqueCount() {
        uniqueCount.incrementAndGet();
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void logUniqueCount() {
        int count = uniqueCount.getAndSet(0); // Reset the count after logging
        String message = "Unique requests count: " + count;

        // Send the count to Kafka
        kafkaProducerService.sendMessage(message);

    }
}
