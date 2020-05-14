package com.example.bast.objects;

import java.util.Date;

public class Log {

    String door;
    Date timeAccessed;
    String pin;
    String card;
    User user;

    public Log(String door, Date timeAccessed) {
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
        return timeAccessed.toString();
    }

    public void setTimeAccessed(Date timeAccessed) {
        this.timeAccessed = timeAccessed;
    }
}
