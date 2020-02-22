package com.example.bast.objects;

import java.util.ArrayList;

public class System {

    private String systemName;
    private String serviceName;
    private boolean isConnected;
    private ArrayList<Lock> locks;
    private ArrayList<User> users;
    private ArrayList<Role> roles;

    //constructor if system is unknown
    public System(String systemServiceName) {
        serviceName = systemServiceName;
        this.systemName = "Unknown System: " + serviceName;
        this.isConnected = false;
    }


    //constructor for known system
    public System(String systemName, boolean connection) {
        this.systemName = systemName;
        this.isConnected = connection;
    }

    // getters and setters
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
