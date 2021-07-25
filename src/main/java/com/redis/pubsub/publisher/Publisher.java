package com.redis.pubsub.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {

    private final RedisTemplate redisTemplate;

    public void publish(String topic, Object object) {
        redisTemplate.convertAndSend(topic, object);
    }
}
