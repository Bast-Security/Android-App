package com.example.bast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class System {

    private String systemName;
    private boolean isConnected;
    private ArrayList<Door> doors;
    private ArrayList<User> users;
    private ArrayList<Role> roles;

    public System() {
        this.systemName = "Unknown System";
        this.isConnected = false;
    }

    public System(String systemName, boolean connection) {
        this.systemName = systemName;
        this.isConnected = connection;
    }

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
