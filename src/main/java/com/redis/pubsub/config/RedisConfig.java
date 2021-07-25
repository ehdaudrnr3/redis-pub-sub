package com.redis.pubsub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.pubsub.annotations.RedisTopicChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(ApplicationContext context) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory());

        Set<String> topicSet = new HashSet<>();
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(RedisTopicChannel.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            MessageListener subscriberListener = (MessageListener) entry.getValue();
            RedisTopicChannel annotation = AnnotationUtils.findAnnotation(subscriberListener.getClass(), RedisTopicChannel.class);

            if(topicSet.contains(annotation.topic())) {
                throw new DuplicateKeyException("duplicate topic:" + annotation.topic());
            }
            redisMessageListenerContainer.addMessageListener(subscriberListener, ChannelTopic.of(annotation.topic()));
            topicSet.add(annotation.topic());
        }
        
        return redisMessageListenerContainer;
    }
}
