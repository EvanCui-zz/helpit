package com.microsoft.helpit.model;

public class PersonalTopicCount {
    private String topic;
    private int count;

    public PersonalTopicCount() {
    }

    public PersonalTopicCount(String topic, int count) {
        this.topic = topic;
        this.count = count;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
