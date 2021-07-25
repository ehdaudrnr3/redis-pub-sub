package com.redis.pubsub.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RedisTopicChannel {

    String topic() default "";

    String desc() default "";
}
