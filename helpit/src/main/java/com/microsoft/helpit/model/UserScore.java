package com.microsoft.helpit.model;

public class UserScore {

    private String upn;
    private double score;

    public UserScore() {
    }

    public UserScore(String upn, double score) {
        this.upn = upn;
        this.score = score;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
