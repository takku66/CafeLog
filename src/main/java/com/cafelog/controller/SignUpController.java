package com.cafelog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.exception.NotAvailableUserException;
import com.cafelog.service.OAuthService;
import com.cafelog.service.SignUpService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignUpController { 

    private final OAuthService oauthService;
    private final SignUpService signUpService;
    
    @RequestMapping(path = "/confirm/signup", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView confirm() {

        ModelAndView mv = new ModelAndView();
        // 外部認証済みのユーザー情報を取得する
        Map<String, String> userInfoMap = new HashMap<>();
        userInfoMap = oauthService.getUserInfoMap();

        // メールアドレスは必須
        String name = userInfoMap.get("name");
        mv.addObject("cafeLogUserName", name);
        mv.setViewName("/signup");
        return mv;
    }
    
    @RequestMapping(path = "/signup", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView doSignup(@RequestParam(value="cafeLogUserName", required = true) String cafeLogUserName) throws NotAvailableUserException {

        ModelAndView mv = new ModelAndView();
        CafeLogUser user = new CafeLogUser();
        user.setName(cafeLogUserName);

        // 外部認証済みのユーザー情報を取得する
        Map<String, String> userInfoMap = new HashMap<>();
        userInfoMap = oauthService.getUserInfoMap();

        // メールアドレスは必須
        String email = userInfoMap.get("email");
        user.setEmail(email);
        signUpService.newUser(user);

        mv.setViewName("redirect:/");
        return mv;
    }

}
