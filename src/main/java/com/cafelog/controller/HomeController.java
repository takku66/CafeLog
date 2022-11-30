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
import com.cafelog.share.JsonConvertor;
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

        List<Cafe> cafedata = cafeLogService.searchFavoriteCafes(userSession.getUser().getUserId());
        return JsonConvertor.toJson(cafedata);
    }
    @ResponseBody
    @RequestMapping(path = "/countapi", method = {RequestMethod.GET, RequestMethod.POST} )
    public String countApi() throws JsonProcessingException{

    
        // TODO: APIの回数制限チェックを別で切り出したい
        // TODO:apiIdを定数から取得するようにする
        if(apiLimitService.isOverLimit(1)){
            Map<String, String> map = new HashMap<>();
            map.put("warning", "APIの使用上限を超えました。");
            return JsonConvertor.toJson(map);
        }
        apiLimitService.count(1, userSession.getUser().getUserId());
        Map<String, String> rtn = new HashMap<>();
        rtn.put("error", "false");
        rtn.put("message", "success countup.");
        return JsonConvertor.toJson(rtn);
    }

    @RequestMapping(path = "/add_spot", method = {RequestMethod.POST} )
    public ModelAndView forwardAddSpot() {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("forward:/add_spot/init");
        return mv;
    }


}
