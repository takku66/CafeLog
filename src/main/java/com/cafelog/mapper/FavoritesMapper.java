package com.cafelog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cafelog.entity.Cafe;

@Mapper
public interface FavoritesMapper {

    @Select("""
        select cafe.cafe_id, cafe.name, cafe.latitude, cafe.longitude
        from favorites as fav
        left outer join cafe
            on fav.user_id = #{user_id} and
                fav.cafe_id = cafe.cafe_id
            """)
    public List<Cafe> findByUserId(int userId);
}
