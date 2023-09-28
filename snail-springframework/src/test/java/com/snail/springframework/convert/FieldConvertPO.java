package com.snail.springframework.convert;

import java.util.Date;

/**
 * @author zhangpengjun
 * @date 2023/9/19
 */
public class FieldConvertPO {

    private String name;

    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FieldConvertPO{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
