package com.example.taskmanager.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class Chores implements Serializable {
    private String name;
    private String message;
    private String date;
    private String user;

    public Chores() {

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }
    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("name", name);
        todo.put("message", message);
        todo.put("date", date);
        todo.put("user", user);

        return todo;
    }

}
