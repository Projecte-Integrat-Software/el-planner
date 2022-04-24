package com.example.our_planner.model;

import java.io.Serializable;

public class Group implements Serializable {
    private String id;
    private String title;
    private String details;
    private int colour;

    public Group(String id, String title, String details, int colour) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.colour = colour;
    }

    public String getTitle() {
        return title;
    }
}
