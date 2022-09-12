package com.cafelog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cafelog.entity.Cafe;
import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.CafeLogProperty;
import com.cafelog.repository.CafeLogUserRepository;
import com.cafelog.repository.FavoritesRepository;
import com.cafelog.repository.CafeLogProperty.PropertyKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CafeLogService {

    private final CafeLogProperty cafeLogProperty;
    private final FavoritesRepository favoritesMapper;
    private final CafeLogUserRepository cafeLogUserMapper;
    
    public String getApiKeyWithDevMode(){
        boolean isGoogleMapDevMode = Boolean.valueOf(cafeLogProperty.get(PropertyKey.GOOGLE_DEV_MODE));
        if(isGoogleMapDevMode){
            return "";
        }else{
            return cafeLogProperty.get(PropertyKey.GOOGLE_MAP_APIKEY);
        }
    }

    public List<Cafe> searchFavoritesCafe(int userId){
        return favoritesMapper.findByUserId(userId);
    }

    public CafeLogUser searchUserByEmail(String email){
        return cafeLogUserMapper.findByEmail(email);
    }
}
