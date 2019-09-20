package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "topic_delete")
public class TopicDeleteModel {
    @Id
    private ObjectId _id;
    private String topic;
    private String voter;//upn

    public TopicDeleteModel() {
    }

    public TopicDeleteModel(String topic, String voter) {
        this.topic = topic;
        this.voter = voter;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }
}
