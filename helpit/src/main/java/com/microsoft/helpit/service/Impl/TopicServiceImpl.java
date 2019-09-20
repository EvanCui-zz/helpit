package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.*;
import com.microsoft.helpit.model.*;
import com.microsoft.helpit.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.microsoft.helpit.util.TimeProcessor.getInterval;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private TopicDeleteDao topicDeleteDao;
    @Autowired
    private TopicAddDao topicAddDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private TopicRelevanceDao topicRelevanceDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    private Lock updateLock = new ReentrantLock();
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicServiceImpl.class);

//    @Async
    @Override
    public List<TopicModel> getAllTopics() {
        LOGGER.info("TopicServiceImpl.getAllTopics");
//        long s = System.currentTimeMillis();
//        List<TopicModel> res = topicDao.findAll(new Sort(Sort.Direction.DESC,"subscribeNum"));
//        System.out.println("getAllTopics: "+(System.currentTimeMillis()-s));
        return topicDao.findAll(new Sort(Sort.Direction.DESC,"subscribeNum"));
    }

//    @Override
//    public List<TopicModel> getTop10Topics() {
//        Pageable page = new PageRequest(0,10,new Sort(Sort.Direction.DESC,"subscribeNum"));
//        return topicDao.findTop10(page).getContent();
//    }

//    @Async
    @Override
    public List<TopicModel> getNewest10Topics() {
        LOGGER.info("TopicServiceImpl.getNewest10Topics");
        Pageable page = new PageRequest(0,10,new Sort(Sort.Direction.DESC,"time"));
        return topicDao.findNewest10(page).getContent();
    }

//    @Async
    @Override
    public List<PersonalTopicCount> getTopTopics(int num) {
        LOGGER.info("TopicServiceImpl.getTopTopics - {}",num);
        Date[] ds = getInterval("month");
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("active_time").lt(etime).gte(stime)),
                    unwind("topics"),
                    group("topics")
                            .count().as("count"),
                    sort(Sort.Direction.DESC,"count"),
                    limit(num),
                    project("count").and("_id").as("topic")
            );
            AggregationResults<PersonalTopicCount> aggregationResults = mongoTemplate.aggregate(aggregation, PostModel.class, PersonalTopicCount.class);
            return aggregationResults.getMappedResults();
        }

        return null;
//        Pageable page = new PageRequest(0,num,new Sort(Sort.Direction.DESC,"subscribeNum"));
//        return topicDao.findTop10(page).getContent();
    }

//    @Async
    @Override
    public List<String> getTopicsByUpn(String upn) {
        LOGGER.info("TopicServiceImpl.getTopicsByUpn - {}",upn);
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        List<String> topics = null;
        if(model!=null){
            topics = model.getTopics();
        }
        return topics;
    }

//    @Override
//    public boolean modifyTopics(SubscriptionModel subscription) {
//        SubscriptionModel model = subscriptionDao.findByUpn(subscription.getUpn());
//        List<String> topics_new = subscription.getTopics();
//        try{
//            updateLock.lock();
//            if(model!=null){
//                List<String> topics_old = model.getTopics();
//                for(String s:topics_old){
//                    TopicModel t = topicDao.findByTopic(s);
//                    t.setSubscribeNum(t.getSubscribeNum()-1);
//                    topicDao.save(t);
//                }
//                model.setTopics(topics_new);
//            }else{
//                model = subscription;
//            }
//
//            for(String s:topics_new){
//                TopicModel t = topicDao.findByTopic(s);
//                t.setSubscribeNum(t.getSubscribeNum()+1);
//                topicDao.save(t);
//            }
//        }finally{
//            updateLock.unlock();
//            subscriptionDao.save(model);
//        }
//
//        return true;
//    }

    @Override
    public boolean subscribe(String upn, String topic) {
        LOGGER.info("TopicServiceImpl.subscribe - {},{}",upn,topic);
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        if(model!=null){
            List<String> topics = model.getTopics();
            if(topics==null)
                topics = new ArrayList<>();
            if(topics.contains(topic))
                return false;
            topics.add(topic);
            model.setTopics(topics);
        }else{
            List<String> ts = new ArrayList<>();
            ts.add(topic);
            List<TimeModel> timeModels = timeDao.findAllSources();
            List<String> sources = new ArrayList<>();
            for(TimeModel t : timeModels){
                sources.add(t.getSource());
            }
            model = new SubscriptionModel(upn,ts,sources,false);
        }
        subscriptionDao.save(model);

        try{
            updateLock.lock();
            TopicModel tmodel = topicDao.findByTopic(topic);
            tmodel.setSubscribeNum(tmodel.getSubscribeNum()+1);
            topicDao.save(tmodel);
        }finally{
            updateLock.unlock();
        }
        return true;
    }

    @Override
    public boolean unsubscribe(String upn, String topic) {
        LOGGER.info("TopicServiceImpl.unsubscribe - {},{}",upn,topic);
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        if(model!=null){
            List<String> topics = model.getTopics();
            if(topics==null||(!topics.contains(topic)))
                return false;
            topics.remove(topic);
            model.setTopics(topics);
        }else{
            return false;
//            List<TimeModel> timeModels = timeDao.findAllSources();
//            List<String> sources = new ArrayList<>();
//            for(TimeModel t : timeModels){
//                sources.add(t.getSource());
//            }
//            model = new SubscriptionModel(upn,null,sources,false);
        }
        subscriptionDao.save(model);

        try{
            updateLock.lock();
            TopicModel tmodel = topicDao.findByTopic(topic);
            tmodel.setSubscribeNum(tmodel.getSubscribeNum()-1);
            topicDao.save(tmodel);
        }finally{
            updateLock.unlock();
        }
        return true;
    }

//    @Override
//    public boolean isSubscribed(String upn, String topic) {
//        SubscriptionModel model = subscriptionDao.findByUpn(upn);
//        if(model!=null) {
//            List<String> topics = model.getTopics();
//            if(topics.contains(topic))
//                return true;
//        }
//        return false;
//    }

    @Override
    public List<TopicDeleteModel> getVotedTopics(String voter) {
        LOGGER.info("TopicServiceImpl.getVotedTopics - {}",voter);
        return topicDeleteDao.findByVoter(voter);
    }

//    @Override
//    public boolean isVoted(String topic, String voter) {
//        return (topicDeleteDao.findByTopicAndVoter(topic,voter)!=null);
//    }

    @Override
    public boolean voteTopic(String topic, String upn) {
        LOGGER.info("TopicServiceImpl.voteTopic - {},{}",topic,upn);
        if(topicDeleteDao.findByTopicAndVoter(topic,upn)==null)
            topicDeleteDao.save(new TopicDeleteModel(topic,upn));
        return true;
    }

    @Override
    public boolean addNewTopics(List<String> topics) {
        LOGGER.info("TopicServiceImpl.addNewTopics - {}",topics.toArray());
        for(String t:topics){
            if(topicAddDao.findByTopic(t)==null){
                topicAddDao.save(new TopicAddModel(t));
            }
        }
        return true;
    }

    @Override
    public boolean undoVote(String topic, String upn) {
        LOGGER.info("TopicServiceImpl.undoVote - {},{}",topic,upn);
        TopicDeleteModel topicDeleteModel = topicDeleteDao.findByTopicAndVoter(topic,upn);
        topicDeleteDao.delete(topicDeleteModel);
        return true;
    }

    @Override
    public List<PersonalTopicCount> getSuggestedTopics(String upn) {
        LOGGER.info("TopicServiceImpl.getSuggestedTopics - {}",upn);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("upn").is(upn)),
                unwind("topic"),
                group("topic")
                        .count().as("count"),
                project("count").and("topic").previousOperation(),
                sort(Sort.Direction.DESC, "count"),
                limit(12)
        );
        AggregationResults<PersonalTopicCount> aggregationResults = mongoTemplate.aggregate(aggregation, AnalysisModel.class, PersonalTopicCount.class);

        List<String> tarr = getTopicsByUpn(upn);
        List<String> res = new ArrayList<>();
        List<PersonalTopicCount> topics = new ArrayList<>();
        if(tarr!=null){
            for(PersonalTopicCount ptc:aggregationResults){
                String t = ptc.getTopic();
                if(!tarr.contains(t)) {
                    topics.add(ptc);
                    res.add(t);
                }
            }
            for(String t : tarr){
                List<TopicRelevanceModel> suggestions = topicRelevanceDao.findDistinctByTopic1OrTopic2(t,t);
                for(TopicRelevanceModel tsm:suggestions){
                    if(!(res.contains(tsm.getTopic1())||res.contains(tsm.getTopic2()))){
                        String stopic = tsm.getTopic1();
                        if(stopic.equals(t))
                            stopic = tsm.getTopic2();
                        res.add(stopic);
                        topics.add(new PersonalTopicCount(stopic,0));
                        if(res.size()>=20){
                            return topics;
                        }
                    }
                }
            }
        }else{
            for(PersonalTopicCount ptc:aggregationResults){
                String t = ptc.getTopic();
                topics.add(ptc);
                res.add(t);
            }
        }

        return topics;
//        return aggregationResults.getMappedResults();
    }
}
