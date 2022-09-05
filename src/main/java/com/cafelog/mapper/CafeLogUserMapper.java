package com.cafelog.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cafelog.entity.CafeLogUser;

@Mapper
public interface CafeLogUserMapper {
    
    public CafeLogUser findByEmail(String email);
}
