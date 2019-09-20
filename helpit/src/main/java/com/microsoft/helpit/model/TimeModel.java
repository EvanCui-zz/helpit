package com.microsoft.helpit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "time_record")
public class TimeModel {
    @Indexed
    private String source;
    private String time;


    public TimeModel(){

    }

    public TimeModel(String source, String time) {
        this.source = source;
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
