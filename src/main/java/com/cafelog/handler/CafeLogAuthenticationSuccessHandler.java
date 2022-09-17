package com.cafelog.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.exception.NotAuthenticatedException;
import com.cafelog.service.CafeLogService;
import com.cafelog.service.OAuthService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CafeLogAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CafeLogService cafeLogService;
    private final OAuthService oauthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            
        // 外部認証済みのユーザー情報を取得する
        Map<String, String> userInfoMap = new HashMap<>();

        try{
            userInfoMap = oauthService.getUserInfoMap();
        }catch(NotAuthenticatedException e){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // メールアドレスでユーザー情報を取得する
        // 未登録だった場合は、新規に登録する
        String email = userInfoMap.get("email");
        CafeLogUser user = cafeLogService.searchUserByEmail(email);
        if(user == null || user.isNotExists()){
            response.sendRedirect(request.getContextPath() + "/confirm/signup");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
    
}
