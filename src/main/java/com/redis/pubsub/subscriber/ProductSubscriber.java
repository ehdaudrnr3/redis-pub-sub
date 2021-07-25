package com.redis.pubsub.subscriber;

import com.redis.pubsub.annotations.RedisTopicChannel;
import com.redis.pubsub.dto.Product;

@RedisTopicChannel(topic = "product", desc = "product subscriber")
public class ProductSubscriber extends AbstractRoutingSubscriber<Product> {

    @Override
    public void listen(String topic, Product message) {
        System.out.println("product = " + message);
    }

}
