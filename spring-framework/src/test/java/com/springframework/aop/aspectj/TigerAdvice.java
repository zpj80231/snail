package com.springframework.aop.aspectj;

import cn.hutool.json.JSONUtil;
import com.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2023/4/19
 */
public class TigerAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("TigerAdvice method before() -> method:" + method.getName() + "(), args:" + JSONUtil.toJsonStr(args));
        System.out.println("TigerAdvice method before() -> 前置aop, 进行一些操作, sleep(2000)");
        Thread.sleep(2000);
    }

}
