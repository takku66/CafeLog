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
import com.cafelog.exception.NotAvailableUserException;
import com.cafelog.repository.UserSessionRepository;
import com.cafelog.service.ApiLimitService;
import com.cafelog.service.CafeLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController { 

    private final CafeLogService cafeLogService;
    private final UserSessionRepository userSession;
    private final ApiLimitService apiLimitService;
    
    
    @RequestMapping(path = "/", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init() throws NotAvailableUserException {

        ModelAndView mv = new ModelAndView();

        mv.addObject("googleMapApiKey", cafeLogService.getApiKeyWithDevMode());

        mv.setViewName("index.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(path = "/allcafe", method = {RequestMethod.GET, RequestMethod.POST} )
    public String fetchCafeData() throws JsonProcessingException{

    
        // TODO: APIの回数制限チェックを別で切り出したい
        // TODO:apiIdを定数から取得するようにする
        if(apiLimitService.isOverLimit(1)){
            Map<String, String> map = new HashMap<>();
            map.put("warning", "APIの使用上限を超えました。");
            return toJson(map);
        }
        apiLimitService.count(1, userSession.getUser().getUserId());


        // List<Cafe> list = cafeRepository.findAll();
        List<Cafe> cafedata = cafeLogService.searchFavoriteCafes(userSession.getUser().getUserId());
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
