package com.example.mobilecomp;

public class ActivityHistory {
    private String activityName;
    private String status;
    private String date;

    public ActivityHistory(String activityName, String status, String date) {
        this.activityName = activityName;
        this.status = status;
        this.date = date;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
