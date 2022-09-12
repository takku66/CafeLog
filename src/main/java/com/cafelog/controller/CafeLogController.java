package com.cafelog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cafelog.entity.Cafe;
import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.UserSessionRepository;
import com.cafelog.service.ApiLimitService;
import com.cafelog.service.CafeLogService;
import com.cafelog.service.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CafeLogController { 

    private final CafeLogService cafeLogService;
    private final OAuthService oauthService;
    private final UserSessionRepository userSession;
    private final ApiLimitService apiLimitService;
    
    @RequestMapping(path = "/login", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView login() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }
    
    @RequestMapping(path = "/", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init() {

        ModelAndView mv = new ModelAndView();

        if(oauthService.isNotAuthenticated()){
            mv.setViewName("redirect:/login");
            return mv;
        }

        try{
            if(oauthService.isAuthenticated() && userSession.isTimeout()){
                Map<String, String> userInfoMap = oauthService.getUserInfoMap();
                String email = userInfoMap.get("email");
                CafeLogUser user = cafeLogService.searchUserByEmail(email);
                userSession.save(user);
            }
        }catch(IllegalStateException e){
            mv.setViewName("redirect:/login");
            return mv;
        }
        
        
        mv.addObject("googleMapApiKey", cafeLogService.getApiKeyWithDevMode());

        mv.setViewName("index.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(path = "/allcafe", method = {RequestMethod.GET, RequestMethod.POST} )
    public String fetchCafeData() throws JsonProcessingException{

        // TODO: Aspectで共通処理化
        if(userSession.isTimeout()){
            Map<String, String> map = new HashMap<>();
            map.put("error", "セッションがありません。");
            return toJson(map);
        }
    
        // TODO: APIの回数制限チェックを別で切り出したい
        // TODO:apiIdを定数から取得するようにする
        if(apiLimitService.isOverLimit(1)){
            Map<String, String> map = new HashMap<>();
            map.put("warning", "APIの使用上限を超えました。");
            return toJson(map);
        }
        apiLimitService.count(1, userSession.getUser().getUserId());


        // List<Cafe> list = cafeRepository.findAll();
        List<Cafe> cafedata = cafeLogService.searchFavoritesCafe(userSession.getUser().getUserId());
        return toJson(cafedata);
    }

    private String toJson(Object object) throws JsonProcessingException{
        if(object == null){
            return "";
        }
        ObjectMapper  objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
