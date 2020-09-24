package com.example.taskmanager.classes;

import java.util.Calendar;

public class Message {
    private String id;
    private String name;
    private String text;
    private String picture;
    private String date = DateUtils.getIso8601StringFromDate(Calendar.getInstance().getTime());
    private transient boolean isMine;

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Message(){

    }

    public Message(String name, String text, String picture){
        this.name =name;
        this.text = text;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
