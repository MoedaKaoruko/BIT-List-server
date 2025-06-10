package com.kf4b.bitlist.converter;

import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;


public class JsonListConverter implements AttributeConverter<Object, String> {
    @Override
    public String convertToDatabaseColumn(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public Object convertToEntityAttribute(String s) {
        return JSON.parseArray(s);
    }
}