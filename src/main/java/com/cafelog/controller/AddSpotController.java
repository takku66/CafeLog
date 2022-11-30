package com.cafelog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cafelog.service.CafeLogService;
import com.cafelog.share.JsonConvertor;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AddSpotController {
    
    private final CafeLogService cafeLogService;

    @RequestMapping(path = "/add_spot/init", method = {RequestMethod.GET, RequestMethod.POST} )
    public ModelAndView init() throws JsonProcessingException{
        ModelAndView mv = new ModelAndView();
        mv.addObject("googleMapApiKey", cafeLogService.getApiKeyWithDevMode());
        mv.setViewName("/add_spot.html");
        return mv;
    }

    @RequestMapping(path = "/add_spot/add", method = {RequestMethod.POST} )
    public String addSpot() throws JsonProcessingException{
        
        Map<String, String> map = new HashMap<>();
        map.put("error", "false");
        return JsonConvertor.toJson(map);
    }
}
