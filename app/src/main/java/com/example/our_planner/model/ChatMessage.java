package com.example.our_planner.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatMessage {

    private String message;
    private String user;
    private String date;

    public ChatMessage(String message, String user) {
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
