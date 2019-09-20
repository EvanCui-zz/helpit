package com.microsoft.helpit.model;

public class PersonalTopicScore {
    private String topic;
    private double score;

    public PersonalTopicScore() {
    }

    public PersonalTopicScore(String topic, double score) {
        this.topic = topic;
        this.score = score;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
