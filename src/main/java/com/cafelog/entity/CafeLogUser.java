package com.cafelog.entity;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@Data
@Component
@SessionScope
public class CafeLogUser {
    
    private Integer userId;
    private String name;
    private String email;

    public boolean isNotExists(){
        return ObjectUtils.isEmpty(userId);
    }

}
