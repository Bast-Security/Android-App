package com.example.bast;

import java.util.ArrayList;

public class Role {

    private String roleName;
    private ArrayList<User> users;

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

    public Role(String roleName) {
        this.roleName = roleName;
        users = new ArrayList<>();
    }
}
