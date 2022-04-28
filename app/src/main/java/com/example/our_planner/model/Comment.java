package com.example.our_planner.model;

import android.annotation.SuppressLint;

import com.example.our_planner.DataBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Comment {

    private String message;
    private String user;
    private String date;

    public Comment(){}

    @SuppressLint("SimpleDateFormat")
    public Comment(String message) {
        this.message = message;
        this.user = DataBaseAdapter.getUserName();
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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
