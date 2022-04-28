package com.example.our_planner.model;

import java.io.Serializable;

public class Group implements Serializable {
    private final String id;
    private String title;
    private String details;
    private int colour;

    public Group(String id, String title, String details, int colour) {
        this.id = id;
        this.setTitle(title);
        this.setDetails(details);
        this.setColour(colour);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }
}
