package com.snail.source.mybatis.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParameterMappingTokenHandler implements TokenHandler {

    /**
     * 按顺序保存 #{} 中对应的字段名称
     */
    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    @Override
    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        return new ParameterMapping(content);
    }

}
