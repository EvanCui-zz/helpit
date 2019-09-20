package com.microsoft.helpit.service;

import com.microsoft.helpit.model.*;

import java.util.List;

public interface ReportService {

    public List<UserScore> getUserScores();

    public List<UserScore> getUserScoresByTime(String interval);

    public List<PersonalTimeScore> getPersonalScoreDistribution(String upn);

    public List<PersonalTimeScore> getPersonalScoreDistribution(String upn,String interval);

    public List<PersonalTopicScore> getPersonalTopicScore(String upn,String interval);

    public List<PersonalTopicCount> getPersonalTopicCount(String upn, String interval);

    public List<PersonalSourceScore> getPersonalSourceScore(String upn);

    public List<PersonalSourceScore> getPersonalSourceScore(String upn, String interval);

    public List<PersonalSourceCount> getPersonalSourceCount(String upn);

    public List<PersonalSourceCount> getPersonalSourceCount(String upn, String interval);

    public List<ReplyCount> getReplyCount(String interval);

    public boolean addVisitCount(String upn);

    public List<ReplyCount> getVisitCount(String interval);
}
