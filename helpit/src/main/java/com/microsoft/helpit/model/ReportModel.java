package com.microsoft.helpit.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "report")
public class ReportModel {
    @Indexed
    private String upn;
    private String source;
    private double score;
    private int count;
    private long date;

    public ReportModel() {
    }

    public ReportModel(String upn, String source, double score, int count, long post_time) {
        this.upn = upn;
        this.source = source;
        this.score = score;
        this.count = count;
        this.date = post_time;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
