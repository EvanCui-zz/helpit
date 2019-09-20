package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "topic")
public class TopicModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String topic;
    private int subscribeNum;
    private Date time;

    public TopicModel() {
    }

    public TopicModel(String topic, int subscribeNum, Date time) {
        this.topic = topic;
        this.subscribeNum = subscribeNum;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getSubscribeNum() {
        return subscribeNum;
    }

    public void setSubscribeNum(int subscribeNum) {
        this.subscribeNum = subscribeNum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
