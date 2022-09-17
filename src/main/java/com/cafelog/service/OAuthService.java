package com.cafelog.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.cafelog.exception.NotAuthenticatedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {
    
    private final OAuth2AuthorizedClientService clientService;

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
        return clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
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
