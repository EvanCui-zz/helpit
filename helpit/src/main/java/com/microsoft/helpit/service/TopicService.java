package com.microsoft.helpit.service;

import com.microsoft.helpit.model.PersonalTopicCount;
import com.microsoft.helpit.model.SubscriptionModel;
import com.microsoft.helpit.model.TopicDeleteModel;
import com.microsoft.helpit.model.TopicModel;

import java.util.List;

public interface TopicService {

    public List<TopicModel> getAllTopics();

//    public List<TopicModel> getTop10Topics();

    public List<TopicModel> getNewest10Topics();

    public List<PersonalTopicCount> getTopTopics(int num);

    public List<String> getTopicsByUpn(String upn);

//    public boolean modifyTopics(SubscriptionModel subscription);

    public boolean subscribe(String upn,String topic);

    public boolean unsubscribe(String upn,String topic);

//    public boolean isSubscribed(String upn,String topic);

    public List<TopicDeleteModel> getVotedTopics(String voter);

//    public boolean isVoted(String topic,String voter);

    public boolean voteTopic(String topic,String upn);

    public boolean addNewTopics(List<String> topics);

    public boolean undoVote(String topic, String upn);

    public List<PersonalTopicCount> getSuggestedTopics(String upn);
}
