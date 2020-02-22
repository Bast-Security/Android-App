package com.example.bast.objects;

import java.util.ArrayList;

public class Role {

    //initialize variables
    private String roleName;
    private ArrayList<User> users;

    // getter and setter methods
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    // constructor
    public Role(String roleName) {
        this.roleName = roleName;
        users = new ArrayList<>();
    }
}
