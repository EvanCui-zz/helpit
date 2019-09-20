package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "topic_add")
public class TopicAddModel {
    @Id
    private ObjectId _id;
    private String topic;
//    private String adder;//upn

    public TopicAddModel() {
    }

    public TopicAddModel(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
