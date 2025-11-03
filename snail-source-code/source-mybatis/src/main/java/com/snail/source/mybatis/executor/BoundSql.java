package com.snail.source.mybatis.executor;

import com.snail.source.mybatis.utils.ParameterMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author zhangpj
 * @date 2025/10/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundSql {

    /**
     * 最终 SQL，占位符替换后的 SQL
     * 如：select * from user where id = ? and username = ?
     */
    private String finalSql;
    /**
     * 按顺序保存的映射参数值
     */
    private List<ParameterMapping> parameterMappings;

}
