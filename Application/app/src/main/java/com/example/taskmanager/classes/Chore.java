package com.example.taskmanager.classes;

public class Chore {
    private String taskName;
    private String assignUser;
    private String dueDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignUser() {
        return assignUser;
    }
}
