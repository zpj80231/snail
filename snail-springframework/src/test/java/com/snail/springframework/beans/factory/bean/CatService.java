package com.snail.springframework.beans.factory.bean;

import com.snail.springframework.beans.factory.annotation.Autowired;
import com.snail.springframework.beans.factory.annotation.Value;
import com.snail.springframework.stereotype.Component;

import java.util.Random;

@Component("catServiceTest")
public class CatService implements Animal {

    @Value("${catServiceNameFromPropertyPlaceholder}")
    private String catServiceNameFromPropertyPlaceholder;

    @Autowired
    private CatDao catDao;

    @Override
    public String getAnimalName() {
        String calls = catDao.queryCalls(new Random().nextInt(3) + 1);
        return catServiceNameFromPropertyPlaceholder + " --> " + calls;
    }

}
