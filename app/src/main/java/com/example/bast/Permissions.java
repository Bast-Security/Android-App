package com.example.bast;

public class Permissions {
    private String timeZone;
    private String credentialType;
    private String target;

    // setting up getter and setter methods
    public void setTimeZone(String timezone){
        timeZone = timezone;
    }
    public String getTimeZone(){
        return timeZone;
    }
    public void setCredentialType(String cred){
        credentialType = cred;
    }
    public String getCredentialType(){
        return credentialType;
    }

    public void setTarget(String Target) {
        target = Target;
    }
    public String getTarget(){
        return target;
    }
}
