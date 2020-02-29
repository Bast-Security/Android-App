package com.example.bast.objects;

import java.util.Date;

public class Lock {

    // intialize variables
    private String lockName;
    private Date dateAdded;
    private Date lastOpened;

    public Lock(String lockName){
        this.lockName = lockName;
    }
    // constructor
    public Lock(String lockName, Date dateAdded) {
        this.lockName = lockName;
        this.dateAdded = dateAdded;
        lastOpened = null;
    }

    // getter and setter methods
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
}
