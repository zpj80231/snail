package com.snail.framework.redis.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * El Parser
 *
 * @author zhangpengjun
 * @date 2024/5/16
 */
public class ElParser {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private ElParser() {

    }

    /**
     * 获取表达式解析后的值
     *
     * @param key  表达式
     * @param args 请求参数
     * @return {@link String }
     */
    public static String parse(String key, Map<String, Object> args) {
        // 通过evaluationContext.setVariable可以在上下文中设定变量
        EvaluationContext context = new StandardEvaluationContext();
        args.forEach(context::setVariable);
        // 解析表达式
        Expression expression = parser.parseExpression(key, new TemplateParserContext());
        // 使用Expression.getValue()获取表达式的值，这里传入了Evaluation上下文
        return expression.getValue(context, String.class);
    }

    /**
     * 获取表达式解析后的值
     *
     * @param method 请求方法
     * @param args   请求参数
     * @param key    表达式
     * @return {@link String }
     */
    public static String parse(Method method, Object[] args, String key) {
        // 解析参数名
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        // 通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            //所有参数都作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        Expression expression = parser.parseExpression(key);
        return expression.getValue(context, String.class);
    }

    /**
     * 获取表达式解析后的值
     * <p/>
     * 如果表达式为空，则使用 类名#方法名#参数值md5值 作为解析后的 key
     *
     * @param point 切入点
     * @param key   表达式
     * @return {@link String }
     */
    public static String parse(ProceedingJoinPoint point, String key) {
        return parse(point, key, true);
    }

    /**
     * 获取表达式解析后的值
     * <p/>
     * 如果表达式为空，则使用 类名#方法名#参数值md5值 作为解析后的 key
     *
     * @param point      切入点
     * @param key        表达式
     * @param appendArgs 表达式为空时，是否追加 参数值md5值 作为解析后的 key
     * @return {@link String }
     */
    public static String parse(ProceedingJoinPoint point, String key, boolean appendArgs) {
        if (StrUtil.isBlank(key)) {
            String className = point.getSignature().getDeclaringTypeName();
            String methodName = point.getSignature().getName();
            key = className + "#" + methodName;
            if (appendArgs) {
                key = className + "#" + methodName + "#" + SecureUtil.md5(Arrays.toString(point.getArgs()));
            }
        } else {
            MethodSignature signature = (MethodSignature) point.getSignature();
            key = ElParser.parse(signature.getMethod(), point.getArgs(), key);
        }
        return key;
    }

    /**
     * 根据前缀、spElKey，自动解析并拼接返回一个完整的字符串
     *
     * @param prefixKey        前缀
     * @param defaultPrefixKey 默认前缀
     * @param spElKey          SP EL 键
     * @param point            点
     * @return {@link String }
     */
    public static String getParseKey(String prefixKey, String defaultPrefixKey, String spElKey, ProceedingJoinPoint point) {
        String keyPrefix = ElParser.getPrefixKey(prefixKey, defaultPrefixKey);
        String splKey = ElParser.parse(point, spElKey);
        return keyPrefix + splKey;
    }

    /**
     * 处理前缀 key，自动拼接 {@code :}
     *
     * @param key        key
     * @param defaultKey 如果 key 为空，那么使用 defaultKey 作为 key
     * @return {@link String }
     */
    public static String getPrefixKey(String key, String defaultKey) {
        if (StrUtil.isBlank(key)) {
            key = defaultKey;
        } else if (key.lastIndexOf(":") == -1) {
            key = key + ":";
        }
        return key;
    }

}
