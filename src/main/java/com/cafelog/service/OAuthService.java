package com.cafelog.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cafelog.exception.NotAuthenticatedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {
    
    private final OAuth2AuthorizedClientService clientService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public boolean isAuthenticated(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }

    public boolean isNotAuthenticated(){
        return this.isAuthenticated() == false;
    }

    public String getUserInfoUri() throws NotAuthenticatedException {
        
        OAuth2AuthorizedClient client = getClient();
        if(client == null) {
            throw new NotAuthenticatedException();
        }
        return client.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUri();
    }

    public OAuth2AuthorizedClient getClient(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return getClient(authentication);
    }
    public OAuth2AuthorizedClient getClient(OAuth2AuthenticationToken authentication){
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        // リフレッシュトークンで再認証
        if(client == null){
            final HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            final HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(authentication.getAuthorizedClientRegistrationId())
            .principal(authentication)
            .attributes(attrs -> {
                attrs.put(HttpServletRequest.class.getName(), req);
                attrs.put(HttpServletResponse.class.getName(), res);
            })
            .build();
            client = this.authorizedClientManager.authorize(authorizeRequest);
            OAuth2AccessToken token = client.getAccessToken();
        }
        
        return client;
    }

    public ResponseEntity<Map> postUserInfoUri(String userInfoEndpointUri, OAuth2AuthorizedClient client){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        return restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
    }

    public Map<String, String> getUserInfoMap() throws NotAuthenticatedException {
        OAuth2AuthorizedClient client = getClient();
        String infoUri = getUserInfoUri();

        if (ObjectUtils.isEmpty(infoUri)) {
            return new HashMap<String, String>();
        }
        ResponseEntity<Map> response = postUserInfoUri(infoUri, client);
        return (Map<String, String>)response.getBody();
    }

    public String getName() throws NotAuthenticatedException {
        return getUserInfoMap().get("name");
    }
    public String getEmail() throws NotAuthenticatedException {
        return getUserInfoMap().get("email");
    }


}
