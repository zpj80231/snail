package com.snail.framework.redis.util;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangpengjun
 * @date 2024/5/16
 */
public class ElParser {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String parse(String spEl, Map<String, Object> args) {
        spEl = String.format("#{%s}", spEl);
        // 通过evaluationContext.setVariable可以在上下文中设定变量
        EvaluationContext context = new StandardEvaluationContext();
        args.forEach(context::setVariable);
        // 解析表达式
        Expression expression = parser.parseExpression(spEl, new TemplateParserContext());
        // 使用Expression.getValue()获取表达式的值，这里传入了Evaluation上下文
        return expression.getValue(context, String.class);
    }

    public static String parse(Method method, Object[] args, String spEl) {
        // 解析参数名
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        // 通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            //所有参数都作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        Expression expression = parser.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

}
