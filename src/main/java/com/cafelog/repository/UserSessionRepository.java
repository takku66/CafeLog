package com.cafelog.repository;

import org.springframework.stereotype.Repository;

import com.cafelog.entity.CafeLogUser;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserSessionRepository {
    
    private final CafeLogUser cafeLogUser;

    public void save(CafeLogUser user){
        cafeLogUser.setUserId(user.getUserId());
        cafeLogUser.setName(user.getName());
        cafeLogUser.setEmail(user.getEmail());
    }

    public CafeLogUser getUser(){
        return this.cafeLogUser;
    }

    public boolean isLogined(){
        return this.cafeLogUser != null;
    }

    public boolean isNotExists(){
        return this.cafeLogUser == null || this.cafeLogUser.getUserId() == null;
    }

    

}
