package com.cafelog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cafelog.entity.Cafe;
import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.CafeLogProperty;
import com.cafelog.repository.CafeLogUserRepository;
import com.cafelog.repository.FavoritesRepository;
import com.cafelog.repository.UserSessionRepository;
import com.cafelog.repository.CafeLogProperty.PropertyKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CafeLogService {

    private final CafeLogProperty cafeLogProperty;
    private final FavoritesRepository favoritesMapper;
    private final CafeLogUserRepository cafeLogUserMapper;
    private final UserSessionRepository userSession;
    
    public String getApiKeyWithDevMode(){
        boolean isGoogleMapDevMode = Boolean.valueOf(cafeLogProperty.get(PropertyKey.GOOGLE_DEV_MODE));
        if(isGoogleMapDevMode){
            return "";
        }else{
            return cafeLogProperty.get(PropertyKey.GOOGLE_MAP_APIKEY);
        }
    }

    public void saveSession(CafeLogUser user){
        userSession.save(user);
    }

    public List<Cafe> searchFavoriteCafes(int userId){
        List<Cafe> favoriteCafes = favoritesMapper.findByUserId(userId);
        if(favoriteCafes == null){
            favoriteCafes = new ArrayList<>();
        }
        return favoriteCafes;
    }

    public CafeLogUser searchUserByEmail(String email){
        CafeLogUser user = cafeLogUserMapper.findByEmail(email);
        if(user == null){
            user = new CafeLogUser();
        }
        return user;
    }

}
