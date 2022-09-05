package com.cafelog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cafelog.entity.Cafe;



@Mapper
public interface CafeMapper {
    
    public List<Cafe> findAll();
}
