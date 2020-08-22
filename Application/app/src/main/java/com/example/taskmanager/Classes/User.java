package com.example.taskmanager.Classes;

import java.io.Serializable;

public class User implements Serializable {
    private String firebaseUserId;
    private House House;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dob;
    private String type;

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
