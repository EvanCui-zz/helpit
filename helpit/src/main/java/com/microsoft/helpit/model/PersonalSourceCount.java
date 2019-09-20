package com.microsoft.helpit.model;

public class PersonalSourceCount {
    private String source;
    private int count;

    public PersonalSourceCount() {
    }

    public PersonalSourceCount(String source, int count) {
        this.source = source;
        this.count = count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
