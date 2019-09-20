package com.microsoft.helpit.controller;

import com.microsoft.helpit.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/source")
public class SourceController {
    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public List<String> getAllSources() {
        return sourceService.getAllSources();
    }

//    @Async
    @RequestMapping(value = "/getSubscribed", method = RequestMethod.POST)
    public List<String> getSubscribedSources(@RequestParam("upn") String upn) {
        return sourceService.getSubscribedSources(upn);
    }

    @RequestMapping(value = "/subscribe",method = RequestMethod.POST)
    public String subscribe(@RequestParam("upn")String upn,@RequestParam("source")String source){
        if(sourceService.subscribeSource(upn,source))
            return "success";
        return "failed";
    }

    @RequestMapping(value = "/unsubscribe",method = RequestMethod.POST)
    public String unsubscribe(@RequestParam("upn")String upn,@RequestParam("source")String source){
        if(sourceService.unsubscribeSource(upn,source))
            return "success";
        return "failed";
    }
}

