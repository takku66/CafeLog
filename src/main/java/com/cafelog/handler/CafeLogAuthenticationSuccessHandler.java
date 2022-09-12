package com.cafelog.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.UserSessionRepository;
import com.cafelog.service.CafeLogService;
import com.cafelog.service.OAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CafeLogAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CafeLogService cafeLogService;
    private final OAuthService oauthService;
    private final UserSessionRepository userSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Map<String, String> userInfoMap = oauthService.getUserInfoMap();
        String email = userInfoMap.get("email");
        CafeLogUser user = cafeLogService.searchUserByEmail(email);
        userSession.save(user);
    }
    
}
