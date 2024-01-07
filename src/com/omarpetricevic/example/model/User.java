package com.omarpetricevic.example.model;

public class User {
    private Long id; // You may not need a setter for this if it's set by the database
    private String fullName;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String userType;
    private String preferredRole;

    // Constructor
    public User(String fullName, String passwordHash, String email, String phoneNumber, String userType, String preferredRole) {
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.preferredRole = preferredRole;
    }

    public User(Long userID, String fullName, String passwordHash, String email, String phoneNumber, String userType, String preferredRole) {
        this.id = userID;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.preferredRole = preferredRole;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    // Consider adding a toString() method for debugging purposes

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType='" + userType + '\'' +
                ", preferredRole='" + preferredRole + '\'' +
                '}';
    }
}

