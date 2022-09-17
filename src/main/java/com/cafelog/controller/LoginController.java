package com.cafelog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController { 

    @RequestMapping(path = "/login", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView login() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

}
