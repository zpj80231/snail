package com.springframework.beans.factory.bean;

import com.springframework.stereotype.Component;

/**
 * @author zhangpengjun
 * @date 2023/9/13
 */
@Component
public class RabbitComponent implements Animal {

    private String nameFromPropertyPlaceholder;

    @Override
    public String getAnimalName() {
        return nameFromPropertyPlaceholder;
    }

}
