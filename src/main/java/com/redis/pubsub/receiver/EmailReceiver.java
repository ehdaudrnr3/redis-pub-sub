package com.redis.pubsub.receiver;

import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class EmailReceiver implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("message = " + message);
    }
}
