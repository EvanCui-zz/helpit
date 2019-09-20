package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.SubscriptionDao;
import com.microsoft.helpit.dao.TimeDao;
import com.microsoft.helpit.model.SubscriptionModel;
import com.microsoft.helpit.model.TimeModel;
import com.microsoft.helpit.service.SourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    private TimeDao timeDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Override
    public List<String> getAllSources() {
        LOGGER.info("SourceServiceImpl.getAllSources");
        List<TimeModel> timeModels = timeDao.findAllSources();
        List<String> sources = new ArrayList<>();
        for(TimeModel t : timeModels){
            sources.add(t.getSource());
        }
        return sources;
    }

    @Override
    public List<String> getSubscribedSources(String upn) {
        LOGGER.info("SourceServiceImpl.getSubscribedSources - {}",upn);
        SubscriptionModel subscriptionModel = subscriptionDao.findByUpn(upn);
        if(subscriptionModel==null){
            List<TimeModel> timeModels = timeDao.findAllSources();
            List<String> sources = new ArrayList<>();
            for(TimeModel t : timeModels){
                sources.add(t.getSource());
            }
            subscriptionModel = new SubscriptionModel(upn,null,sources,false);
            subscriptionDao.save(subscriptionModel);
        }
        return subscriptionModel.getSources();
    }

    @Override
    public boolean subscribeSource(String upn, String source) {
        LOGGER.info("SourceServiceImpl.subscribeSource - {},{}",upn,source);
        SubscriptionModel subscriptionModel = subscriptionDao.findByUpn(upn);
        if(subscriptionModel!=null){
            List<String> sources = subscriptionModel.getSources();
            if(sources==null)
                sources = new ArrayList<>();
            if(sources.contains(source))
                return false;
            sources.add(source);
            subscriptionModel.setSources(sources);
        }else{
            List<String> sources = new ArrayList<>();
            sources.add(source);
            subscriptionModel = new SubscriptionModel(upn,null,sources,false);
        }

        subscriptionDao.save(subscriptionModel);
        return true;
    }

    @Override
    public boolean unsubscribeSource(String upn, String source) {
        LOGGER.info("SourceServiceImpl.unsubscribeSource - {},{}",upn,source);
        SubscriptionModel subscriptionModel = subscriptionDao.findByUpn(upn);
        if(subscriptionModel!=null){
            List<String> sources = subscriptionModel.getSources();
            if(sources==null||(!sources.contains(source)))
                return false;
            sources.remove(source);
            subscriptionModel.setSources(sources);
        }else{
           return false;
        }

        subscriptionDao.save(subscriptionModel);
        return true;
    }
}
