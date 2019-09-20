package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "post")
public class PostModel {
    @Id
    private ObjectId _id;
    @Indexed
    private long threadId;
    private String title;
    @Indexed
    private List<String> topics;
    private String source;
    private String web_url;
    private Date create_time;
    private String creator_name;
    private String creator_url;
    private Date active_time;
    private String updator_name;
    private String updator_url;

    public PostModel(){

    }

    public PostModel(long threadId, String title, List<String> topics, String source, String url, Date create_time, String creator_name, String creator_url, Date active_time, String updator_name, String updator_url) {
        this.threadId = threadId;
        this.title = title;
        this.topics = topics;
        this.source = source;
        this.web_url = url;
        this.create_time = create_time;
        this.creator_name = creator_name;
        this.creator_url = creator_url;
        this.active_time = active_time;
        this.updator_name = updator_name;
        this.updator_url = updator_url;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreator_url() {
        return creator_url;
    }

    public void setCreator_url(String creator_url) {
        this.creator_url = creator_url;
    }

    public Date getActive_time() {
        return active_time;
    }

    public void setActive_time(Date active_time) {
        this.active_time = active_time;
    }

    public String getUpdator_name() {
        return updator_name;
    }

    public void setUpdator_name(String updator_name) {
        this.updator_name = updator_name;
    }

    public String getUpdator_url() {
        return updator_url;
    }

    public void setUpdator_url(String updator_url) {
        this.updator_url = updator_url;
    }
}
