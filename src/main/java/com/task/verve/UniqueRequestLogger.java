package com.task.verve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UniqueRequestLogger {

    @Bean
    public AtomicInteger atomicInteger() {
        return new AtomicInteger(0);
    }

    private static final Logger logger = LoggerFactory.getLogger(UniqueRequestLogger.class);
    private final AtomicInteger uniqueCount;
    private final ConcurrentMap<Integer, Boolean> uniqueIds;

    @Autowired
    public UniqueRequestLogger(AtomicInteger uniqueCount, ConcurrentMap<Integer, Boolean> uniqueIds) {
        this.uniqueCount = uniqueCount;
        this.uniqueIds = uniqueIds;
    }

    @Scheduled(fixedRate = 60000) // 1 minute interval
    public void logUniqueRequestCount() {
        int count = uniqueCount.getAndSet(0); // Get and reset count
        uniqueIds.clear(); // Clear the map
        logger.info("Unique request count in the last minute: {}", count);
    }
}
