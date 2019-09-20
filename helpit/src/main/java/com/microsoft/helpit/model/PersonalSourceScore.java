package com.microsoft.helpit.model;

public class PersonalSourceScore {
    private String source;
    private double score;

    public PersonalSourceScore() {
    }

    public PersonalSourceScore(String source, double score) {
        this.source = source;
        this.score = score;
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
}
