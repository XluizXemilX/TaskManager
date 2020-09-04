package com.example.taskmanager.classes;

import com.example.taskmanager.R;

public class Profile {
    private String nickname;
    private String type;
    private String id ;
    private String accountId;
    private String profilePin;
    private int picture;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPin() {
        return profilePin;
    }

    public void setUserPin(String userPin) {
        this.profilePin = userPin;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
