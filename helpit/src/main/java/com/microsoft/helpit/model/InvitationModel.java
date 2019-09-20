package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "invitation")
public class InvitationModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String sender;
    @Indexed
    private String receiver;
    private String title;
    private String web_url;
    private Date time;
    private boolean status;

    public InvitationModel() {
    }

    public InvitationModel(String sender, String receiver, String title, String web_url, Date time, boolean status) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.web_url = web_url;
        this.time = time;
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
