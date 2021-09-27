package com.example.ginkgo.entity;

import java.io.Serializable;

public class Alarm implements Serializable {

    private String userId;
    private String alarmId;
    private String alarmName ;
    private String time ;
    private boolean isOn  ;

    public Alarm() {
    }

    public Alarm(String userId, String alarmNameId, String alarmName, String time, boolean isOn) {
        this.userId = userId;
        this.alarmId = alarmNameId;
        this.alarmName = alarmName;
        this.time = time;
        this.isOn = isOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
