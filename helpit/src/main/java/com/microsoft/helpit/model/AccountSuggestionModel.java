package com.microsoft.helpit.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account_suggestion")
public class AccountSuggestionModel {

    @Indexed
    private String upn;
    private String name;
    private long account;
    private String display_name;
    private String source;
    private double relevance;

    public AccountSuggestionModel() {
    }

    public AccountSuggestionModel(String upn, String name, long account, String display_name, String source, double relevance) {
        this.upn = upn;
        this.name = name;
        this.account = account;
        this.display_name = display_name;
        this.source = source;
        this.relevance = relevance;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }
}
