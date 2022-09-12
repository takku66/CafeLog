package com.cafelog.entity;

import java.io.Serializable;


import lombok.Data;

@Data
public class ApiLimitManage implements Serializable {

    private Integer apiId;

    private String apiName ;

    private Integer limitCount;

    private String limitIntervalType;

}
