package com.springframework.beans.factory.bean;

import com.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CatDao {

    private static Map<Integer, String> hashMap = new HashMap<>();

    static {
        hashMap.put(1, "喵！");
        hashMap.put(2, "喵！喵！");
        hashMap.put(3, "喵！喵！喵！");
    }

    public String queryCalls(int number) {
        return hashMap.get(number);
    }

}
