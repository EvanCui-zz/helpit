package com.microsoft.helpit.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "topic_relevance")
public class TopicRelevanceModel {

    @Indexed
    private String topic1;
    @Indexed
    private String topic2;
    private double relevance;

    public TopicRelevanceModel() {
    }

    public TopicRelevanceModel(String topic1, String topic2, double relevance) {
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.relevance = relevance;
    }

    public String getTopic1() {
        return topic1;
    }

    public void setTopic1(String topic1) {
        this.topic1 = topic1;
    }

    public String getTopic2() {
        return topic2;
    }

    public void setTopic2(String topic2) {
        this.topic2 = topic2;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }
}
