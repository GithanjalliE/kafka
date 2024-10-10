package com.task.verve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/verve")
public class VerveController {

    public enum REQUEST_TYPE {
        GET, POST;
    }

    @Autowired
    private StringRedisTemplate redisTemplate; // Inject Redis template
    REQUEST_TYPE request_type = REQUEST_TYPE.POST;

    private static final Logger logger = LoggerFactory.getLogger(VerveController.class);
    private final ConcurrentHashMap<Integer, Boolean> uniqueIds = new ConcurrentHashMap<>();
    private final AtomicInteger uniqueCount = new AtomicInteger(0);
    private static final String ID_PREFIX = "request_id_"; // Key prefix for Redis

    @GetMapping("/accept")
    public ResponseEntity<String> acceptRequest(
            @RequestParam int id,
            @RequestParam(required = false) String endpoint) {
        try {
            // Check Redis if ID has already been processed
            String key = ID_PREFIX + id;
            Boolean isNewId = redisTemplate.opsForValue().setIfAbsent(key, "1", 1, TimeUnit.MINUTES);

            if (Boolean.TRUE.equals(isNewId)) {
                // ID is unique within the time window
                uniqueCount.incrementAndGet();

                // Optional: Fire HTTP request to external endpoint if provided
                if (endpoint != null && !endpoint.isEmpty()) {
                    fireHttpRequest(endpoint);
                }

                return ResponseEntity.ok("ok");
            } else {
                // ID has already been processed
                return ResponseEntity.ok("Duplicate request, ignoring.");
            }
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed");
        }
    }


    private void fireHttpRequest(String endpoint) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest get_request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "?count=" + uniqueCount.get()))
                .build();

            String jsonData = "{\"uniqueCount\":" + uniqueCount.get() + "}";

            HttpRequest post_request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = null;
            if(request_type == REQUEST_TYPE.GET) {
                response = client.send(get_request, HttpResponse.BodyHandlers.ofString());
            }
            else if(request_type == REQUEST_TYPE.POST) {
                response = client.send(post_request, HttpResponse.BodyHandlers.ofString());
            }


            logger.info("HTTP GET to {} returned status code {}", endpoint, response.statusCode());
        } catch (Exception e) {
            logger.error("Failed to send HTTP GET request to {}", endpoint, e);
        }
    }


}
