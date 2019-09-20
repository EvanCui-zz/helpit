package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.PersonalTopicCount;
import com.microsoft.helpit.model.TopicDeleteModel;
import com.microsoft.helpit.model.TopicModel;
import com.microsoft.helpit.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public List<TopicModel> getAllTopics(){
        return topicService.getAllTopics();
    }

    @RequestMapping(value = "/getByUpn",method = RequestMethod.POST)
    public List<String> getTopicsByUpn(@RequestParam("upn")String upn){
        return topicService.getTopicsByUpn(upn);
    }

//    @RequestMapping(value = "/getTop10",method = RequestMethod.POST)
//    public List<TopicModel> getTop10Topics(){
//        return topicService.getTopTopics(10);
//    }

    @RequestMapping(value = "/getTop",method = RequestMethod.POST)
    public List<PersonalTopicCount> getTopTopics(@RequestParam("num") int num){
        return topicService.getTopTopics(num);
    }

    @RequestMapping(value = "/getNewest10",method = RequestMethod.POST)
    public List<TopicModel> getNewest10Topics(){
        return topicService.getNewest10Topics();
    }

    @RequestMapping(value = "/getVoted",method = RequestMethod.POST)
    public List<TopicDeleteModel> getVotedTopics(@RequestParam("upn")String upn){
        return topicService.getVotedTopics(upn);
    }

    @RequestMapping(value = "/vote",method = RequestMethod.POST)
    public boolean voteTopic(@RequestParam("topic")String topic, @RequestParam("upn")String upn){
        return topicService.voteTopic(topic,upn);
    }

    @RequestMapping(value = "/addNew",method = RequestMethod.POST)
    public boolean addNewTopic(@RequestBody List<String> topics){
        return topicService.addNewTopics(topics);
    }

    @RequestMapping(value = "/undoVote",method = RequestMethod.POST)
    public boolean undoVoteTopic(@RequestParam("topic")String topic, @RequestParam("upn")String upn){
        return topicService.undoVote(topic,upn);
    }

    @RequestMapping(value = "/getSuggestion",method = RequestMethod.POST)
    public List<PersonalTopicCount> getSuggestion(@RequestParam("upn")String upn){
        return topicService.getSuggestedTopics(upn);
    }
}
