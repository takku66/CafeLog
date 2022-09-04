package com.cafelog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.cafelog.entity.Cafe;
import com.cafelog.entity.Sample;
import com.cafelog.repository.CafeLogProperty;
import com.cafelog.repository.CafeRepository;
import com.cafelog.repository.SampleRepository;
import com.cafelog.repository.CafeLogProperty.PropertyKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CafeLogController { 

    
    private final SampleRepository sampleRepository;
    
    private final CafeRepository cafeRepository;

    private final CafeLogProperty cafeLogProperty;

    private final OAuth2AuthorizedClientService clientService;
    
    @RequestMapping(path = "/", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init() {
        ModelAndView mv = new ModelAndView();

        boolean isGoogleMapDevMode = Boolean.valueOf(cafeLogProperty.get(PropertyKey.GOOGLE_DEV_MODE));
        if(isGoogleMapDevMode){
            mv.addObject("googleMapApiKey", "");
        }else{
            mv.addObject("googleMapApiKey", cafeLogProperty.get(PropertyKey.GOOGLE_MAP_APIKEY));
        }
        
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient client = clientService
                                        .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                                            .getProviderDetails()
                                            .getUserInfoEndpoint()
                                            .getUri();
        if (!ObjectUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity <Map>response = restTemplate
                .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            mv.addObject("name", userAttributes.get("name"));
        }

        mv.setViewName("index.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(path = "/allcafe", method = {RequestMethod.GET, RequestMethod.POST} )
    public String fetchCafeData() throws JsonProcessingException{

        List<Cafe> list = cafeRepository.findAll();

        ObjectMapper  objectMapper = new ObjectMapper();
        String cafedata = objectMapper.writeValueAsString(list);

        return cafedata;
    }

    @RequestMapping(path = "/db/connect/test", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView dbConnectTest(){
        ModelAndView mv = new ModelAndView();

        List<Sample> list = sampleRepository.findAll();
        int maxId = list.get(list.size()-1).getId();

        Sample sd = new Sample();
        sd.setId(maxId + 1);
        sd.setName("SampleName From Application" + (maxId + 1));
        sampleRepository.save(sd);

        mv.setViewName("index.html");
        return mv;
    }


}
