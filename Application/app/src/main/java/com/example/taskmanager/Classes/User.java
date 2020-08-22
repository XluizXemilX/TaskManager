package com.example.taskmanager.Classes;

import java.io.Serializable;

public class User implements Serializable {
    private String firebaseUserId;
    private String firstName;
    private Integer pin;
    private String type;

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public void setFirebaseUserId(String firebaseUserId) {
        this.firebaseUserId = firebaseUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
