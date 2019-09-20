package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "visit_count")
public class VisitCountModel {

    @Id
    private ObjectId _id;
    @Indexed
    private String upn;
    private int count;
    private long date;

    public VisitCountModel() {
    }

    public VisitCountModel(String upn, int count, long date) {
        this.upn = upn;
        this.count = count;
        this.date = date;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
