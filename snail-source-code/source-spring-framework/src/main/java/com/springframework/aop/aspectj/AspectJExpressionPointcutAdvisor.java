package com.springframework.aop.aspectj;

import com.springframework.aop.Pointcut;
import com.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 切面拦截器，PointcutAdvisor 的实现。
 * 将切面表达式，切面，匹配后的执行操作进行封装实现，后续就可以配置为一个 bean 加载到容器中方便使用。
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private Advice advice;

    private String expression;

    private AspectJExpressionPointcut aspectJExpressionPointcut;

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (aspectJExpressionPointcut == null) {
            aspectJExpressionPointcut = new AspectJExpressionPointcut(expression);
        }
        return aspectJExpressionPointcut;
    }

}
