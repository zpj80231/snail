package com.snail.framework.redis.config;

import com.snail.framework.redis.duplicate.DefaultDuplicateSubmitHandler;
import com.snail.framework.redis.duplicate.DuplicateSubmitAspect;
import com.snail.framework.redis.duplicate.DuplicateSubmitHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
@EnableAspectJAutoProxy
public class DuplicateSubmitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DuplicateSubmitHandler duplicateSubmitHandler() {
        return new DefaultDuplicateSubmitHandler();
    }

    @Bean
    @ConditionalOnBean({RedisTemplate.class, DuplicateSubmitHandler.class})
    public DuplicateSubmitAspect duplicateSubmitAspect(RedisTemplate<Object, Object> redisTemplate,
                                                       DuplicateSubmitHandler duplicateSubmitHandler) {
        return new DuplicateSubmitAspect(redisTemplate, duplicateSubmitHandler);
    }

}
