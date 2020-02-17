package com.example.bast;

import java.util.Date;

public class Door {

    private String doorName;
    private Date dateAdded;
    private Date lastOpened;

    public Door(String doorName, Date dateAdded) {
        this.doorName = doorName;
        this.dateAdded = dateAdded;
        lastOpened = null;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorNamedoorName(String doorName) {
        this.doorName = doorName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(Date lastOpened) {
        this.lastOpened = lastOpened;
    }
}
