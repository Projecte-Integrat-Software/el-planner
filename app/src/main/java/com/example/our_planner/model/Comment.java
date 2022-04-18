package com.example.our_planner.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Comment {

    private String message;
    private String user;
    private String date;

    @SuppressLint("SimpleDateFormat")
    public Comment(String message, String user) {
        this.message = message;
        this.user = user;
        this.date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }
}
