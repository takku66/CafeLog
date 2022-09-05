package com.cafelog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cafelog.entity.Cafe;
import com.cafelog.entity.CafeLogUser;
import com.cafelog.mapper.CafeLogUserMapper;
import com.cafelog.mapper.FavoritesMapper;
import com.cafelog.repository.CafeLogProperty;
import com.cafelog.repository.CafeRepository;
import com.cafelog.repository.SampleRepository;
import com.cafelog.repository.CafeLogProperty.PropertyKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CafeLogService {

    private final CafeLogProperty cafeLogProperty;

    private final CafeRepository cafeRepository;

    private final FavoritesMapper favoritesMapper;
    private final CafeLogUserMapper cafeLogUserMapper;
    
    public String getApiKeyWithDevMode(){
        boolean isGoogleMapDevMode = Boolean.valueOf(cafeLogProperty.get(PropertyKey.GOOGLE_DEV_MODE));
        if(isGoogleMapDevMode){
            return "";
        }else{
            return cafeLogProperty.get(PropertyKey.GOOGLE_MAP_APIKEY);
        }
    }

    public String searchFavoritesCafeJson(int userId) throws JsonProcessingException{
        List<Cafe> list = searchFavoritesCafe(userId);
        ObjectMapper  objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(list);
    }
    public List<Cafe> searchFavoritesCafe(int userId){
        return favoritesMapper.findByUserId(userId);
    }

    public CafeLogUser searchUserByEmail(String email){
        return cafeLogUserMapper.findByEmail(email);
    }
}
