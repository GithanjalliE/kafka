package com.task.verve;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
    public class AtomicCounterService {

    private final AtomicInteger atomicInteger = new AtomicInteger();

        private AtomicInteger counter;

        public AtomicCounterService() {
            this.counter = new AtomicInteger(0);
        }

        public void increment() {
            counter.incrementAndGet();
        }

        public int get() {
            return counter.get();
        }
    }