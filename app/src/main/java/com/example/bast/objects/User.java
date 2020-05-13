package com.example.bast.objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {

    private int userID;
    private String userName;
    private String email;
    private String phoneNumber;
    private String pin;
    private String cardNumber;
    private ArrayList<Role> roles;

    // constructor
    public User(int userID, String userName, String email, String phoneNumber, String pin, String cardNumber) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.cardNumber = cardNumber;
        this.roles = new ArrayList<>();
    }

    //getter and setters
    public int getUserID(){return userID;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(Role...role){
        for (Role i:role) {
            roles.add(i);
        }
    }

}
