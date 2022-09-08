package com.cafelog.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.cafelog.service.OAuthService;

import lombok.RequiredArgsConstructor;

/**
 * Auth0のログアウト処理をする。
 * 参考：https://auth0.com/docs/quickstart/webapp/java-spring-boot/01-login
 */
@Controller
@RequiredArgsConstructor
public class LogoutHandler extends SecurityContextLogoutHandler {

    // private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuthService oauthService;

    /**
     * {@linkplain SecurityContextLogoutHandler} に処理を委譲してアプリケーションとAuth0からログアウトする。
     *
     * @param httpServletRequest the request.
     * @param httpServletResponse the response.
     * @param authentication the current authentication.
     */
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {

        // セッションの無効化と、コンテキストの破棄をする
        super.logout(httpServletRequest, httpServletResponse, authentication);

        String issuer = (String) oauthService.getClient().getClientRegistration().getProviderDetails().getConfigurationMetadata().get("issuer");

        // ログアウトさせるためのリダイレクト用のURLを作る
        // サンプルURL： https://YOUR-DOMAIN/v2/logout?clientId=YOUR-CLIENT-ID&returnTo=http://localhost:8080/
        // String issuer = (String) getClientRegistration().getProviderDetails().getConfigurationMetadata().get("issuer");
        String clientId = oauthService.getClient().getClientRegistration().getClientId();
        String returnTo = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

        String logoutUrl = UriComponentsBuilder
                .fromHttpUrl(issuer + "v2/logout?client_id={clientId}&returnTo={returnTo}")
                .encode()
                .buildAndExpand(clientId, returnTo)
                .toUriString();

        try {
            httpServletResponse.sendRedirect(logoutUrl);
        } catch (IOException ioe) {
            // Handle or log error redirecting to logout URL
        }
    }

}