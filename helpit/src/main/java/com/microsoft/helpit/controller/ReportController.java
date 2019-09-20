package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.*;
import com.microsoft.helpit.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

//    @RequestMapping(value = "/getUserScores",method = RequestMethod.POST)
//    public List<UserScore> getUserScores(){
//        return reportService.getUserScores();
//    }

    @RequestMapping(value = "/getUserScoresByTime",method = RequestMethod.POST)
    public List<UserScore> getUserScoresByTime(@RequestParam("interval") String interval){
        if(interval.equals("all"))
            return reportService.getUserScores();
        return reportService.getUserScoresByTime(interval);
    }

    @RequestMapping(value = "/getPersonalScoreByTime",method = RequestMethod.POST)
    public List<PersonalTimeScore> getPersonalScoreDistribution(@RequestParam("upn") String upn,@RequestParam("interval") String interval){
        if(interval.equals("all"))
            return reportService.getPersonalScoreDistribution(upn);
        return reportService.getPersonalScoreDistribution(upn,interval);
    }

    @RequestMapping(value = "/getPersonalTopicScore",method = RequestMethod.POST)
    public List<PersonalTopicScore> getPersonalTopicScore(@RequestParam("upn") String upn, @RequestParam("interval") String interval){
        return reportService.getPersonalTopicScore(upn,interval);
    }

    @RequestMapping(value = "/getPersonalTopicCount",method = RequestMethod.POST)
    public List<PersonalTopicCount> getPersonalTopicCount(@RequestParam("upn") String upn, @RequestParam("interval") String interval){
        return reportService.getPersonalTopicCount(upn,interval);
    }

    @RequestMapping(value = "/getPersonalSourceScore",method = RequestMethod.POST)
    public List<PersonalSourceScore> getPersonalSourceScore(@RequestParam("upn") String upn, @RequestParam("interval") String interval){
        if(interval.equals("all"))
            return reportService.getPersonalSourceScore(upn);
        return reportService.getPersonalSourceScore(upn,interval);
    }

    @RequestMapping(value = "/getPersonalSourceCount",method = RequestMethod.POST)
    public List<PersonalSourceCount> getPersonalSourceCount(@RequestParam("upn") String upn, @RequestParam("interval") String interval){
        if(interval.equals("all"))
            return reportService.getPersonalSourceCount(upn);
        return reportService.getPersonalSourceCount(upn,interval);
    }

    @RequestMapping(value = "/getReplyCount",method = RequestMethod.POST)
    public List<ReplyCount> getReplyCount(@RequestParam("interval") String interval){
        return reportService.getReplyCount(interval);
    }

    @RequestMapping(value = "/addVisitCount",method = RequestMethod.POST)
    public boolean addVisitCount(@RequestParam("upn") String upn){
        return reportService.addVisitCount(upn);
    }

    @RequestMapping(value = "/getVisitCount",method = RequestMethod.POST)
    public List<ReplyCount> getVisitCount(@RequestParam("interval") String interval){
        return reportService.getVisitCount(interval);
    }
}
