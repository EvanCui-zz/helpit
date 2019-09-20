package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "subscription")
public class SubscriptionModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String upn;
    private List<String> topics;
    private List<String> sources;
    private boolean sendEmail;

    public SubscriptionModel(){

    }

    public SubscriptionModel(String upn, List<String> topics, List<String> sources, boolean sendEmail) {
        this.upn = upn;
        this.topics = topics;
        this.sources = sources;
        this.sendEmail = sendEmail;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
}
