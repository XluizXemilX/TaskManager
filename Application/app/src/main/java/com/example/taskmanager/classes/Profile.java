package com.example.taskmanager.classes;

import java.util.Map;
import java.util.Objects;

public class Profile {
    private String nickname;
    private String type;
    private String id ;
    private String accountId;
    private String profilePin;
    private String picture;
    private String bank;
    private String balance;

    public Profile() {}

    public Profile(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String money) {
        this.balance = money;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

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

    public String getProfilePin() {
        return profilePin;
    }

    public void setProfilePin(String userPin) {
        this.profilePin = userPin;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return nickname.equals(profile.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    public void fromMap(Map<String, String> map){
        this.nickname = map.get("nickname");
        this.type = map.get("type");
        this.accountId = map.get("accountId");
        this.picture = map.get("picture");
        this.profilePin = map.get("profilePin");
        this.id = map.get("id");
        this.balance = map.get("balance");
        this.bank = map.get("bank");
    }
}
