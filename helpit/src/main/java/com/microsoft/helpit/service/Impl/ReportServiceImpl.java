package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.VisitCountDao;
import com.microsoft.helpit.model.*;
import com.microsoft.helpit.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.microsoft.helpit.util.TimeProcessor.getInterval;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private VisitCountDao visitCountDao;
//    @Autowired
//    private AnalysisDao analysisDao;
//    @Autowired
//    private ReportDao reportDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public List<UserScore> getUserScores() {
        Aggregation aggregation = newAggregation(
                group("upn")
                        .sum("score").as("score"),
                project("score").and("upn").previousOperation()
        );
        AggregationResults<UserScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, UserScore.class);
        LOGGER.info("ReportServiceImpl.getUserScores");
        return aggregationResults.getMappedResults();
    }

    @Override
    public List<UserScore> getUserScoresByTime(String interval) {
        LOGGER.info("ReportServiceImpl.getUserScoresByTime - {}",interval);
        Date[] ds = getInterval(interval);
        if(ds!=null){
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("date").lt(etime.getTime()).gte(stime.getTime())),
                    group("upn")
                            .sum("score").as("score"),
                    project("score").and("upn").previousOperation()
            );
            AggregationResults<UserScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, UserScore.class);

            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public List<PersonalTimeScore> getPersonalScoreDistribution(String upn) {
//        List<ReportModel> models = reportDao.findByUpn(upn);
//        List<PersonalTimeScore> res = models.stream()
//                .map(e -> new PersonalTimeScore(e.getPost_time(), e.getCount(),e.getScore()))
//                .collect(Collectors.toList());
        LOGGER.info("ReportServiceImpl.getPersonalScoreDistribution - {}",upn);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("upn").is(upn)),
                group("date")
                        .sum("score").as("y")
                        .sum("count").as("count"),
                project("y","count").and("_id").as("x")
        );

        AggregationResults<PersonalTimeScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalTimeScore.class);
        return aggregationResults.getMappedResults();
//        List<AnalysisModel> models = analysisDao.findByUpn(upn);
//        return getPersonalTimeScoreList(models);
    }

    @Override
    public List<PersonalTimeScore> getPersonalScoreDistribution(String upn, String interval) {
        LOGGER.info("ReportServiceImpl.getPersonalScoreDistribution - {},{}",upn,interval);
        Date[] ds = getInterval(interval);
        if(ds!=null){
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("upn").is(upn).and("date").lt(etime.getTime()).gte(stime.getTime())),
                    group("date")
                            .sum("score").as("y")
                            .sum("count").as("count"),
                    project("y","count").and("_id").as("x")
            );

            AggregationResults<PersonalTimeScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalTimeScore.class);
            return aggregationResults.getMappedResults();

//            List<AnalysisModel> models = analysisDao.findByUpnAndPost_time(upn,stime,etime);
//            return getPersonalTimeScoreList(models);
        }
        return null;
    }

    @Override
    public List<PersonalTopicScore> getPersonalTopicScore(String upn,String interval) {
        LOGGER.info("ReportServiceImpl.getPersonalTopicScore - {},{}",upn,interval);
        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("post_time").lt(etime).gte(stime).and("upn").is(upn)),
                    unwind("topic"),
                    group("topic")
                            .sum("score").as("score"),
                    project("score").and("topic").previousOperation()
            );
            AggregationResults<PersonalTopicScore> aggregationResults = mongoTemplate.aggregate(aggregation, AnalysisModel.class, PersonalTopicScore.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public List<PersonalTopicCount> getPersonalTopicCount(String upn, String interval) {
        LOGGER.info("ReportServiceImpl.getPersonalTopicCount - {},{}",upn,interval);
        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("post_time").lt(etime).gte(stime).and("upn").is(upn)),
                    unwind("topic"),
                    group("topic")
                            .count().as("count"),
                    project("count").and("topic").previousOperation()
            );
            AggregationResults<PersonalTopicCount> aggregationResults = mongoTemplate.aggregate(aggregation, AnalysisModel.class, PersonalTopicCount.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public List<PersonalSourceScore> getPersonalSourceScore(String upn) {
        LOGGER.info("ReportServiceImpl.getPersonalSourceScore - {}",upn);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("upn").is(upn)),
                group("source")
                        .sum("score").as("score"),
                project("score").and("source").previousOperation()
        );
        AggregationResults<PersonalSourceScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalSourceScore.class);
        return aggregationResults.getMappedResults();
    }

    @Override
    public List<PersonalSourceScore> getPersonalSourceScore(String upn, String interval) {
        LOGGER.info("ReportServiceImpl.getPersonalSourceScore - {},{}",upn,interval);
        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("date").lt(etime.getTime()).gte(stime.getTime()).and("upn").is(upn)),
                    group("source")
                            .sum("score").as("score"),
                    project("score").and("source").previousOperation()
            );
            AggregationResults<PersonalSourceScore> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalSourceScore.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public List<PersonalSourceCount> getPersonalSourceCount(String upn) {
        LOGGER.info("ReportServiceImpl.getPersonalSourceCount - {}",upn);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("upn").is(upn)),
                group("source")
                        .count().as("count"),
                project("count").and("source").previousOperation()
        );
        AggregationResults<PersonalSourceCount> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalSourceCount.class);
        return aggregationResults.getMappedResults();
    }

    @Override
    public List<PersonalSourceCount> getPersonalSourceCount(String upn, String interval) {
        LOGGER.info("ReportServiceImpl.getPersonalSourceCount - {},{}",upn,interval);
        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("date").lt(etime.getTime()).gte(stime.getTime()).and("upn").is(upn)),
                    group("source")
                            .count().as("count"),
                    project("count").and("source").previousOperation()
            );
            AggregationResults<PersonalSourceCount> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, PersonalSourceCount.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public List<ReplyCount> getReplyCount(String interval) {
        LOGGER.info("ReportServiceImpl.getReplyCount - {}",interval);

        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("date").lt(etime.getTime()).gte(stime.getTime())),
                    group("upn")
                            .sum("count").as("tcount"),
                    sort(Sort.Direction.DESC,"tcount"),
                    project("tcount").and("upn").previousOperation()
            );
            AggregationResults<ReplyCount> aggregationResults = mongoTemplate.aggregate(aggregation, ReportModel.class, ReplyCount.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    @Override
    public boolean addVisitCount(String upn) {
        LOGGER.info("ReportServiceImpl.addVisitCount - {}",upn);
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        VisitCountModel model = visitCountDao.findByUpnAndDate(upn,today.getTime().getTime());
        if(model==null){
            visitCountDao.save(new VisitCountModel(upn,1,today.getTime().getTime()));
        }else{
            model.setCount(model.getCount()+1);
            visitCountDao.save(model);
        }
        return true;
    }

    @Override
    public List<ReplyCount> getVisitCount(String interval) {
        LOGGER.info("ReportServiceImpl.getVisitCount - {}",interval);

        Date[] ds = getInterval(interval);
        if(ds!=null) {
            Date stime = ds[0];
            Date etime = ds[1];

            Aggregation aggregation = newAggregation(
                    match(Criteria.where("date").lt(etime.getTime()).gte(stime.getTime())),
                    group("upn")
                            .sum("count").as("tcount"),
                    sort(Sort.Direction.DESC,"tcount"),
                    project("tcount").and("upn").previousOperation()
            );
            AggregationResults<ReplyCount> aggregationResults = mongoTemplate.aggregate(aggregation, VisitCountModel.class, ReplyCount.class);
            return aggregationResults.getMappedResults();
        }
        return null;
    }

    private List<PersonalTimeScore> getPersonalTimeScoreList(List<AnalysisModel> models){
        Map<Long,PersonalTimeScore> map = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        for(AnalysisModel a :models){
            Date d = a.getPost_time();
            try {
                d = formatter.parse(formatter.format(d));
                long time = d.getTime();
                if(map.get(time)==null){
                    map.put(time,new PersonalTimeScore(time,1,a.getScore()));
                }else{
                    PersonalTimeScore p = map.get(time);
                    int count = p.getCount();
                    double score = p.getY();
                    p.setCount(count+1);
                    p.setY(score+a.getScore());
                    map.put(time,p);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Long> sortedKeys = new ArrayList<>(map.keySet());
        Collections.sort(sortedKeys);

        List<PersonalTimeScore> res = new ArrayList<>();
        for(long time:sortedKeys){
            res.add(map.get(time));
        }

        return res;
    }
}
