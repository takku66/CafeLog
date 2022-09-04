package com.cafelog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auth0のログアウト処理をする。
 * 参考：https://auth0.com/docs/quickstart/webapp/java-spring-boot/01-login
 * ちょっと日本語に訳した
 */
@Controller
public class LogoutHandler extends SecurityContextLogoutHandler {

    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * {@code ClientRegistrationRepository} を受け取ってインスタンスを生成する。
     * これでAuth0のログアウトエンドポイントをコールすることができる。
     *
     * @param clientRegistrationRepository the {@code ClientRegistrationRepository} for this application.
     */
    @Autowired
    public LogoutHandler(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

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

        // ログアウトさせるためのリダイレクト用のURLを作る
        // サンプルURL： https://YOUR-DOMAIN/v2/logout?clientId=YOUR-CLIENT-ID&returnTo=http://localhost:8080/
        String issuer = (String) getClientRegistration().getProviderDetails().getConfigurationMetadata().get("issuer");
        String clientId = getClientRegistration().getClientId();
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

    /**
     * Gets the Spring ClientRegistration, which we use to get the registered client ID and issuer for building the
     * {@code returnTo} query parameter when calling the Auth0 logout API.
     *
     * @return the {@code ClientRegistration} for this application.
     */
    private ClientRegistration getClientRegistration() {
        return this.clientRegistrationRepository.findByRegistrationId("auth0");
    }
}