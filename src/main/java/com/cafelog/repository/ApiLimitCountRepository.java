package com.cafelog.repository;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cafelog.entity.ApiLimitCount;

@Mapper
public interface ApiLimitCountRepository {

    @Select("""
    select count(*)
    from api_limit_count
    where api_id = #{apiId} 
        and called_at between #{startDateTime} and #{endDateTime}
    """)
    public int calculateCurrentCount(int apiId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Insert("""
    insert into api_limit_count
    values(
        #{apiId}, 
        current_timestamp, 
        #{userId})
    """)
    public int save(ApiLimitCount apiLimitCount);
}
