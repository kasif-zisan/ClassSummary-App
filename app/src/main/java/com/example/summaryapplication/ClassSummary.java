package com.example.summaryapplication;

public class ClassSummary {
    String ID = "";
    String course = "";
    String type = "";
    long date;
    String lecture = "";
    String topic = "";
    String summary = "";

    public ClassSummary(String ID, String course, String type, long date, String lecture, String topic, String summary){
        this.ID = ID;
        this.course = course;
        this.type = type;
        this.date = date;
        this.lecture = lecture;
        this.topic = topic;
        this.summary = summary;
    }
}
