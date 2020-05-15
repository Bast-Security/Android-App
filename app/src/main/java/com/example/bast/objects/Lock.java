package com.example.bast.objects;

import java.util.Date;

public class Lock {

    // intialize variables
    private int lockId;
    private String lockName;
    private Date dateAdded;
    private Date lastOpened;
    private int mode = 2;

    public Lock(int lockId, String lockName) {
        this.lockName = lockName;
        this.lockId = lockId;
    }

    // getter and setter methods
    public int getLockId(){return lockId;}

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
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

    public int getMode() { return mode; }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
