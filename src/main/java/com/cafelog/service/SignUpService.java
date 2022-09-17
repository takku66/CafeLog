package com.cafelog.service;

import org.springframework.stereotype.Service;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.CafeLogUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final CafeLogUserRepository cafeLogUserRepository;

    public CafeLogUser newUser(CafeLogUser user){        
        cafeLogUserRepository.save(user);
        return cafeLogUserRepository.findByEmail(user.getEmail());
    }

}
