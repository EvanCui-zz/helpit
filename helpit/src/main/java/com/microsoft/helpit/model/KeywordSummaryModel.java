package com.microsoft.helpit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "keyword_summary")
public class KeywordSummaryModel {
    @Id
    private ObjectId _id;
    @Indexed
    private String keyword;
    private int num;

    public KeywordSummaryModel() {
    }

    public KeywordSummaryModel(String keyword, int num) {
        this.keyword = keyword;
        this.num = num;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
