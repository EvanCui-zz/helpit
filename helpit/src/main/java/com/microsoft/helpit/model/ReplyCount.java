package com.microsoft.helpit.model;

public class ReplyCount {
    private String upn;
    private int tcount;

    public ReplyCount() {
    }

    public ReplyCount(String upn, int tcount) {
        this.upn = upn;
        this.tcount = tcount;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public int getTcount() {
        return tcount;
    }

    public void setTcount(int tcount) {
        this.tcount = tcount;
    }
}
