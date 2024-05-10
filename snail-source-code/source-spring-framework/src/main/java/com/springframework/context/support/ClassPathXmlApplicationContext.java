package com.springframework.context.support;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private final String[] configLocations;

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

}
