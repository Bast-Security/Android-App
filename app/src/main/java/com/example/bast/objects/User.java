package com.example.bast.objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {

    private String userName;
    private String email;
    private String phoneNumber;
    private int pin;
    private int cardNumber;
    private ArrayList<Role> roles;

    // constructor
    public User(String userName, String email, String phoneNumber, int pin, int cardNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.cardNumber = cardNumber;
        this.roles = new ArrayList<>();
    }

    //getter and setters
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

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

}
