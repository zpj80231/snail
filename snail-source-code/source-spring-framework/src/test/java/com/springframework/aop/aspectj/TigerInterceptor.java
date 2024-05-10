package com.springframework.aop.aspectj;

import cn.hutool.core.thread.ThreadUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 自定义方法拦截处理
 *
 * @author zhangpengjun
 * @date 2023/4/7
 */
public class TigerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("~方法执行前~");
        Object result = invocation.proceed();
        ThreadUtil.sleep(100);
        System.out.println("~方法执行后~ 返回结果：" + result);
        return result;
    }

}
