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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {
    
    private final OAuth2AuthorizedClientService clientService;

    public String getUserInfoUri(){
        
        OAuth2AuthorizedClient client = getClient();
        return client.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUri();
    }

    public OAuth2AuthorizedClient getClient(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    }

    public ResponseEntity<Map> postUserInfoUri(String userInfoEndpointUri, OAuth2AuthorizedClient client){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        return restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
    }

    public Map<String, String> getUserInfoMap(){
        OAuth2AuthorizedClient client = getClient();
        String infoUri = getUserInfoUri();

        if (ObjectUtils.isEmpty(infoUri)) {
            return new HashMap<String, String>();
        }
        ResponseEntity<Map> response = postUserInfoUri(infoUri, client);
        return (Map<String, String>)response.getBody();
    }

    public String getName(){
        return getUserInfoMap().get("name");
    }
    public String getEmail(){
        return getUserInfoMap().get("email");
    }


}
