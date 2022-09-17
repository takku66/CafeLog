package com.cafelog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cafelog.entity.CafeLogUser;

@Mapper
public interface CafeLogUserRepository {
    
    
    @Select("""
    select user_id, name, email
    from users
    where email = #{email}
    """)
    public CafeLogUser findByEmail(String email);

    @Select("""
        select usr_id, name, email from users
            """)
    public List<CafeLogUser> findAll();

    @Select("""
        select usr_id, name, email from users where user_id=#{userId}
            """)
    public List<CafeLogUser> findById(int userId);

    @Insert("""
        insert into users(name, email) values(#{name}, #{email})
            """)
    public int save(CafeLogUser user);

    @Update("""
        update users set name=#{name}, email=#{email} where user_id=#{user_id}
            """)
    public int update(CafeLogUser user);

    @Delete("""
        delete from users where user_id=#{userId}
            """)
    public int deleteById(int userId);
}
