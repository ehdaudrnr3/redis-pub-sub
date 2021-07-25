package com.redis.pubsub.controller;

import com.redis.pubsub.publisher.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final Publisher publisher;

    @PostMapping("/publish/{topic}")
    public void publish(@PathVariable String topic, @RequestBody Object object) {
        publisher.publish(topic, object);
    }
}
