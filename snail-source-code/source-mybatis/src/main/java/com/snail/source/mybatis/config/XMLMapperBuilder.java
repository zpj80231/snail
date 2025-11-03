package com.snail.source.mybatis.config;

import com.snail.source.mybatis.pojo.Configuration;
import com.snail.source.mybatis.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 封装 mapper.xml 文件解析逻辑
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream mapperInputStream) throws Exception {
        // 解析流并获取根节点，即 <mapper>
        Document document = new SAXReader().read(mapperInputStream);
        Element rootElement = document.getRootElement();

        // ----- 解析 mapper 文件 -----
        /*
            如：<select id="selectOne" resultType="com.snail.source.mybatis.entity.User"
                        parameterType="com.snail.source.mybatis.entity.User">
                    select * from user where id = #{id} and name = #{name}
                </select>
            1. 获取 namespace，拼装 statementId
            2. 解析 mapper sql 语句，封装为 MapperStatement 对象
            3. 将 MapperStatement 对象设置到 Configuration 中
         */
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectList = rootElement.elements("select");
        for (Element element : selectList) {
            String value = element.attributeValue("id");
            String statementId = namespace + "." + value;
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sql = element.getTextTrim();

            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setStatementId(statementId);
            mapperStatement.setResultType(resultType);
            mapperStatement.setParameterType(parameterType);
            mapperStatement.setSql(sql);
            mapperStatement.setSqlCommandType("select");

            configuration.getMapperStatementMap().put(statementId, mapperStatement);
        }
    }

}
