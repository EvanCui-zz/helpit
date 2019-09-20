package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.AccountModel;
import com.microsoft.helpit.model.AccountSuggestionModel;
import com.microsoft.helpit.model.SubscriptionModel;
import com.microsoft.helpit.service.TopicService;
import com.microsoft.helpit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

//    @RequestMapping(value = "/modifySubscription",method = RequestMethod.POST)
//    public String modifySubscription(@RequestBody SubscriptionModel subscription){
//        if(topicService.modifyTopics(subscription))
//            return "success";
//        return "failed";
//    }

    @RequestMapping(value = "/subscribe",method = RequestMethod.POST)
    public String subscribe(@RequestParam("upn")String upn,@RequestParam("topic")String topic){
        if(topicService.subscribe(upn,topic))
            return "success";
        return "failed";
    }

    @RequestMapping(value = "/unsubscribe",method = RequestMethod.POST)
    public String unsubscribe(@RequestParam("upn")String upn,@RequestParam("topic")String topic){
        if(topicService.unsubscribe(upn,topic))
            return "success";
        return "failed";
    }

//    @RequestMapping(value = "/isSubscribed",method = RequestMethod.POST)
//    public boolean isSubscribed(@RequestParam("upn")String upn,@RequestParam("topic")String topic){
//        return topicService.isSubscribed(upn,topic);
//    }

    @RequestMapping(value = "/getAccounts",method = RequestMethod.POST)
    public List<AccountModel> getAccounts(@RequestParam("upn")String upn){
//        userService.initAccounts(upn,username);
        return userService.getAccounts(upn);
    }

    @RequestMapping(value = "/checkAccount",method = RequestMethod.POST)
    public boolean checkAccount(@RequestParam("id")Long id,@RequestParam("source")String source){
        return userService.checkAccount(id,source);
    }

    @RequestMapping(value = "/modifyAccounts",method = RequestMethod.POST)
    public boolean modifyAccounts(@RequestBody List<AccountModel> accounts){
        userService.modifyAccount(accounts);
        return true;
    }

    @RequestMapping(value = "/getSuggestedAccounts",method = RequestMethod.POST)
    public List<AccountSuggestionModel> getSuggestedAccounts(@RequestParam("upn")String upn){
        return userService.getSuggestedAccounts(upn);
    }

    @RequestMapping(value = "/isExisted",method = RequestMethod.POST)
    public boolean isExisted(@RequestParam("upn")String upn){
        return userService.isExisted(upn);
    }

    @RequestMapping(value = "/isBlank",method = RequestMethod.POST)
    public boolean isBlank(@RequestParam("upn")String upn){
//        userService.initAccounts(upn,username);
        return userService.isBlank(upn);
    }

    @RequestMapping(value = "/getNumber",method = RequestMethod.POST)
    public int getNumber(){
//        userService.initAccounts(upn,username);
        return userService.getNumber();
    }

    @RequestMapping(value = "/changeSendEmail",method = RequestMethod.POST)
    public boolean changeSendEmail(@RequestParam("upn")String upn,@RequestParam("sendEmail")boolean sendEmail){
        return userService.changeSendEmail(upn,sendEmail);
    }

    @RequestMapping(value = "/getSendEmail",method = RequestMethod.POST)
    public boolean getSendEmail(@RequestParam("upn")String upn){
        return userService.getSendEmail(upn);
    }
}
