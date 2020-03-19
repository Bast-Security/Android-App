package com.example.bast.objects;

public class AuthFailedException extends Exception {
    public AuthFailedException(String message) {
        super(message);
    }

    public AuthFailedException() {
        super("Authentication failed.");
    }
}
