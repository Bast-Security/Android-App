package com.example.bast.objects;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Permissions {

    private User userName;
    private Lock lockName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DayOfWeek[] listOfDays;
    private boolean isAllowed = false;

    // constructor
    //TODO add this to database
    public void Permissions(Lock lock, LocalDateTime permissionStartTime,
                LocalDateTime permissionEndTime, DayOfWeek[] listOfDaysPermitted){
        lockName = lock;
        startTime = permissionStartTime;
        endTime = permissionEndTime;
        listOfDays = listOfDaysPermitted;
    }

    // checks if user is allowed
    public boolean isAllowed(){
        return isAllowed;
    }


}
