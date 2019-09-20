package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "keyword_record")
public class KeywordModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String keyword;
    @Indexed
    private String upn;//upn
    private Date time;
    private int num;

    public KeywordModel() {
    }

    public KeywordModel(String keyword, String upn, Date time, int num) {
        this.keyword = keyword;
        this.upn = upn;
        this.time = time;
        this.num = num;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
