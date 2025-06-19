package com.snail.framework.redis.config.duplicate;

import com.snail.framework.redis.duplicate.DuplicateSubmit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2024/5/20
 */
@Slf4j
public class Eagle {

    private int count = 0;

    public int getCount() {
        return count;
    }

    /**
     * 3秒内不允许重复
     */
    @DuplicateSubmit(expire = 3L)
    public void fly() {
        count++;
        log.info("fly...");
    }

    /**
     * key是EL表达式解析后的值（默认1秒不允许重复）
     */
    @DuplicateSubmit(key = "#rounds")
    public void flyCount(int rounds) {
        for (int i = 0; i < rounds; i++) {
            count++;
            log.info("fly...");
        }
    }

}
