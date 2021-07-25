package com.redis.pubsub.subscriber;

import com.redis.pubsub.annotations.RedisTopicChannel;

@RedisTopicChannel(topic = "product")
public class duplecheck extends AbstractRoutingSubscriber {
    @Override
    public void listen(String topic, Object message) {
        System.out.println("topic = " + topic);
    }
}
