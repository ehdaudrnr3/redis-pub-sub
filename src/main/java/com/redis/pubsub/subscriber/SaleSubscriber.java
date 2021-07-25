package com.redis.pubsub.subscriber;

import com.redis.pubsub.annotations.RedisTopicChannel;
import com.redis.pubsub.dto.Sale;
import org.springframework.data.redis.connection.Message;

@RedisTopicChannel(topic = "sale", desc = "sale subscriber")
public class SaleSubscriber extends AbstractRoutingSubscriber<Sale> {

    @Override
    public void listen(String topic, Sale message) {
        System.out.println("message = " + message);
    }
}
