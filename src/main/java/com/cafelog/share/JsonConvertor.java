package com.cafelog.share;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConvertor {
    public static String toJson(Object object) throws JsonProcessingException{
        if(object == null){
            return "";
        }
        ObjectMapper  objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
