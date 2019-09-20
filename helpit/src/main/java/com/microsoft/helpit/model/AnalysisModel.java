package com.microsoft.helpit.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "analysis")
public class AnalysisModel {

    private long userId;
    @Indexed
    private String upn;
    private String source;
    private double score;
    private boolean is_accepted;
    private int comment_count;
    private List<String> topic;
    private Date post_time;

    public AnalysisModel() {
    }

    public AnalysisModel(long userId, String upn, String source, double score, boolean is_accepted, int comment_count, List<String> topic, Date post_time) {
        this.userId = userId;
        this.upn = upn;
        this.source = source;
        this.score = score;
        this.is_accepted = is_accepted;
        this.comment_count = comment_count;
        this.topic = topic;
        this.post_time = post_time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public List<String> getTopic() {
        return topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }
}
