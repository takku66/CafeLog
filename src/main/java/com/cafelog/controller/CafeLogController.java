package com.cafelog.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.service.CafeLogService;
import com.cafelog.service.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CafeLogController { 

    private final CafeLogService cafeLogService;
    private final OAuthService oauthService;
    private final CafeLogUser cafeLogUser;
    
    @RequestMapping(path = "/", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init() {

        ModelAndView mv = new ModelAndView();

        mv.addObject("googleMapApiKey", cafeLogService.getApiKeyWithDevMode());
        Map<String, String> userInfoMap = oauthService.getUserInfoMap();
        String email = userInfoMap.get("email");
        CafeLogUser user = cafeLogService.searchUserByEmail(email);

        cafeLogUser.setUserId(user.getUserId());
        cafeLogUser.setName(user.getName());
        cafeLogUser.setEmail(user.getEmail());

        mv.setViewName("index.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(path = "/allcafe", method = {RequestMethod.GET, RequestMethod.POST} )
    public String fetchCafeData() throws JsonProcessingException{

        // List<Cafe> list = cafeRepository.findAll();
        String cafedata = cafeLogService.searchFavoritesCafeJson(cafeLogUser.getUserId());

        return cafedata;
    }

    @RequestMapping(path = "/db/connect/test", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView dbConnectTest(){
        ModelAndView mv = new ModelAndView();

        // List<Sample> list = sampleRepository.findAll();
        // int maxId = list.get(list.size()-1).getId();

        // Sample sd = new Sample();
        // sd.setId(maxId + 1);
        // sd.setName("SampleName From Application" + (maxId + 1));
        // sampleRepository.save(sd);

        mv.setViewName("index.html");
        return mv;
    }


}
