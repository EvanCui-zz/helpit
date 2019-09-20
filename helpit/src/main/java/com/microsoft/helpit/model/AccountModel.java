package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
public class AccountModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String upn;
    private String name;
    @Indexed
    private String source;
    private Long account;

    public AccountModel() {
    }

    public AccountModel(String upn, String name, String source, Long account) {
        this.upn = upn;
        this.name = name;
        this.source = source;
        this.account = account;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }
}
