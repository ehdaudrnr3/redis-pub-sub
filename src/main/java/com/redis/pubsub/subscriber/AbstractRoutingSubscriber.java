package com.redis.pubsub.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.pubsub.annotations.RedisTopicChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public abstract class AbstractRoutingSubscriber<T> implements SubscribeListener<T> {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    protected T deserialize(Message message) {
        try {
            Object deserialize = redisTemplate.getStringSerializer().deserialize(message.getBody());
            Class<?> genericType = getGenericType();
            if(genericType == null) {
                return (T) deserialize;
            }
            return (T) mapper.readValue(deserialize.toString(), getGenericType());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisTopicChannel annotation = getAnnotation();

        log.info("consume topic : {}", annotation.topic());
        listen(annotation.topic(), deserialize(message));
    }

    private RedisTopicChannel getAnnotation() {
        return AnnotationUtils.findAnnotation(this.getClass(), RedisTopicChannel.class);
    }

    private Class<?> getGenericType() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if(genericSuperclass instanceof  ParameterizedType) {
            Type actualType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            return (Class<?>) actualType;
        }
        return null;
    }
}
