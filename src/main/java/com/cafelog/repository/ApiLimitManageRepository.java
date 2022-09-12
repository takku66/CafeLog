package com.cafelog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cafelog.entity.ApiLimitManage;

@Mapper
public interface ApiLimitManageRepository {

    @Select("""
    select * 
    from api_limit_manage
    """)
    public List<ApiLimitManage> findAll();

    @Select("""
    select * 
    from api_limit_manage
    where api_id = #{apiId}
    """)
    public ApiLimitManage findById(int apiId);

}
