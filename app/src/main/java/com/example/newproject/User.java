package com.example.newproject;
public class User {
    private String userId;

    private String name;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String email, String name) {
        this.userId = userId;

        this.name = name;
    }

    // Add getter and setter methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
