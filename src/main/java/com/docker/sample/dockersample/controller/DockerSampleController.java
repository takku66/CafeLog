package com.docker.sample.dockersample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DockerSampleController {
    
    @RequestMapping(path = "/home", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        return mv;
    }
}
