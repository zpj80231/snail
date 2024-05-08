package com.snail.framework.async;

import com.snail.framework.async.config.AsyncConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Configuration
@Import(AsyncConfig.class)
public class SnailAsyncAutoConfiguration {

}
