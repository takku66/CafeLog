package com.docker.sample.dockersample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.docker.sample.dockersample.entity.Sample;
import com.docker.sample.dockersample.repository.SampleRepository;

@Controller
public class DockerSampleController {

    @Autowired
    SampleRepository repository;
    
    @RequestMapping(path = "/home", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView init(){
        ModelAndView mv = new ModelAndView();



        mv.setViewName("index.html");
        return mv;
    }

    @RequestMapping(path = "/db/connect/test", method = {RequestMethod.GET,RequestMethod.POST} )
    public ModelAndView dbConnectTest(){
        ModelAndView mv = new ModelAndView();

        List<Sample> list = repository.findAll();
        int maxId = list.get(list.size()-1).getId();

        Sample sd = new Sample();
        sd.setId(maxId + 1);
        sd.setName("SampleName From Application" + (maxId + 1));
        repository.save(sd);

        mv.setViewName("index.html");
        return mv;
    }


}
