package com.cafelog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cafelog.entity.CafeLogUser;

@Mapper
public interface CafeLogUserMapper {
    
    
    @Select("""
    select user_id, name, email
    from users
    where email = #{email}
    """)
    public CafeLogUser findByEmail(String email);
}
