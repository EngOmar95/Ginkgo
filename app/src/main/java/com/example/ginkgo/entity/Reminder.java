package com.example.ginkgo.entity;

import java.io.Serializable;
import java.util.Date;

public class Reminder implements Serializable {
    private String userId;
    private String reminderId;
    private String reminderName;
    private String time;
    private Date day;
    private String location;
    private String Commint;
    private String attachment;
    private boolean isOn;

    public Reminder() {
    }

    public Reminder(String userId, String reminderId, String reminderName, String time, Date day, String location, String commint, String attachment, boolean isOn) {
        this.userId = userId;
        this.reminderId = reminderId;
        this.reminderName = reminderName;
        this.time = time;
        this.day = day;
        this.location = location;
        Commint = commint;
        this.attachment = attachment;
        this.isOn = isOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCommint() {
        return Commint;
    }

    public void setCommint(String commint) {
        Commint = commint;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
