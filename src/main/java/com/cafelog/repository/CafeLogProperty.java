package com.cafelog.repository;

import java.util.HashMap;
import java.util.Map;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;


@Component
@Configuration
@RequiredArgsConstructor
public class CafeLogProperty {

    private final Environment environment;

    public enum PropertyKey {
        GOOGLE_MAP_APIKEY("google.map.api-key"),
        GOOGLE_DEV_MODE("google.map.develop-mode");

        String key;

        PropertyKey(String key){
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }


    private Map<String, String> propertyMap = new HashMap<>();

    public String get(PropertyKey property){
        return findValue(property);
    }

    private String findValue(PropertyKey property){
        if( !propertyMap.containsKey(property.getKey())){
            if(ObjectUtils.isEmpty(environment.getProperty(property.getKey()))){
                propertyMap.put(property.getKey(), "");
            }else{
                propertyMap.put(property.getKey(), environment.getProperty(property.getKey()));
            }
        }
            return propertyMap.get(property.getKey());
    }

    
}
