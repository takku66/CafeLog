package com.cafelog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cafelog.entity.Cafe;

@Mapper
public interface FavoritesRepository {

    @Select("""
        select cafe.cafe_id, cafe.name, cafe.latitude as lat, cafe.longitude as lng
        from (select * from favorites where user_id=#{user_id}) as fav
        left outer join cafe
            on fav.cafe_id = cafe.cafe_id
            """)
    public List<Cafe> findByUserId(int userId);
}
