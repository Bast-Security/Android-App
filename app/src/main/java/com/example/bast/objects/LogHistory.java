package com.example.bast.objects;

import java.util.Date;

public class LogHistory {

    String door;
    String timeAccessed;
    String pin;
    String card;
    User user;

    public LogHistory(String door, String timeAccessed) {
        this.door = door;
        this.timeAccessed = timeAccessed;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getTimeAccessed() {
        return timeAccessed;
    }

    public void setTimeAccessed(String timeAccessed) {
        this.timeAccessed = timeAccessed;
    }
}
