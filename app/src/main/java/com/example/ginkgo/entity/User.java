package com.example.ginkgo.entity;

import java.util.Date;

public class User {
    private String userId;
    private String email;
    private String password;
    private String nickName;
    private String gender;
    private String brithOfDate;

    public User() {
    }

    public User(String userId,String email, String password, String nickName, String gender, String brithOfDate) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.brithOfDate = brithOfDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBrithOfDate() {
        return brithOfDate;
    }

    public void setBrithOfDate(String brithOfDate) {
        this.brithOfDate = brithOfDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
