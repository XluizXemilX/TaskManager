package com.example.taskmanager.classes;

public class Chore {
    private String taskName;
    private String assignUser;
    private String userPhoto;
    private String dueDate;
    private String accountId;
    private String photoRequired = "false";
    private String verificationRequire = "false";
    private String id;
    private String type;
    private String picture;
    private String status;
    private String description;
    private String recurrence;
    private String time;
    private String startDate;
    private String pay;
    private String pictureProof;

    public String getPictureProof() {
        return pictureProof;
    }

    public void setPictureProof(String pictureProof) {
        this.pictureProof = pictureProof;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPhotoRequired() {
        return photoRequired;
    }

    public void setPhotoRequired(String photoRequired) {
        this.photoRequired = photoRequired;
    }

    public String getVerificationRequire() {
        return verificationRequire;
    }

    public void setVerificationRequire(String verificationRequire) {
        this.verificationRequire = verificationRequire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
