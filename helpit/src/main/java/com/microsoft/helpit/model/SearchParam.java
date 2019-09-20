package com.microsoft.helpit.model;

import java.util.Date;
import java.util.List;

public class SearchParam {

    private List<String> topics;
    private int page;
    private List<String> sources;
//    private Date start;
    private String keywords;

    public SearchParam() {
    }

    public SearchParam(List<String> topics, int page, List<String> sources) {
        this.topics = topics;
        this.page = page;
        this.sources = sources;
//        this.start = start;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    //    public Date getStart() {
//        return start;
//    }
//
//    public void setStart(Date start) {
//        this.start = start;
//    }
}
