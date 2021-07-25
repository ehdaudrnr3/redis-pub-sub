package com.redis.pubsub.subscriber;

import org.springframework.data.redis.connection.MessageListener;

public interface SubscribeListener<T> extends MessageListener {
    void listen(String topic, T message);
}
