package com.snail.source.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.snail.source.mybatis.io.Resources;
import com.snail.source.mybatis.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 封装 XML 配置文件解析逻辑
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class XMLConfigBuilder {

    private final Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    public Configuration parse(InputStream inputStream) throws Exception {
        // 将 xml 转为 document
        Document document = new SAXReader().read(inputStream);
        // 获取根节点，即 <configuration>
        Element rootElement = document.getRootElement();

        // ----- 解析 数据库 配置 -----
        /*
            1. 获取 <property> 节点
            2. 解析并构建为数据源对象
            3. 将数据源对象设置到 Configuration 中
        */
        // '//'表示匹配当前所有节点，而不考虑它们的位置
        // 如：<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        Element dataSourceElement = rootElement.element("dataSource");
        List<Element> propertyList = dataSourceElement.elements("property");
        Properties properties = new Properties();
        for (Element element : propertyList) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }
        // 构建数据源对象
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        // 将构建好的数据源对象设置到 Configuration 中
        configuration.setDataSource(dataSource);

        // ----- 解析 Mapper Resource 配置 -----
        /*
           1. 获取 <mapper> 节点的 resource 属性值，即 mapper.xml 文件的相对路径
           2. 解析 mapper 文件，将 mapper.xml 文件中的 statement 配置解析为 MapperStatement 对象
           3. 将 MapperStatement 对象设置到 Configuration 中
        */
        // 如：<mapper resource="com/snail/source/mybatis/mapper/UserMapper.xml"></mapper>
        Element mappers = rootElement.element("mappers");
        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            String resource = element.attributeValue("resource");
            InputStream mapperInputStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(mapperInputStream);
        }

        return configuration;
    }

}
