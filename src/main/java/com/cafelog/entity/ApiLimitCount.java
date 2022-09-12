package com.cafelog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiLimitCount implements Serializable {

    private Integer apiId;

    private LocalDateTime calledAt ;

    private Integer userId;

}
