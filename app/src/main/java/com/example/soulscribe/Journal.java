package com.example.soulscribe;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thoughts;
    private String imageUrl;
    private String userID;
    private Timestamp timeadded;
    private String UserName;

    public Journal() {
    }

    public Journal(String title, String thoughts, String imageUrl, String userID, Timestamp timeadded, String userName) {
        this.title = title;
        this.thoughts = thoughts;
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.timeadded = timeadded;
        UserName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        this.timeadded = timeadded;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
