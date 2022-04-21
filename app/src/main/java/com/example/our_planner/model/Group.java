package com.example.our_planner.model;

public class Group {
    private String title;
    private String details;
    private int colour;

    public Group(String title, String details, int colour) {
        this.title = title;
        this.details = details;
        this.colour = colour;
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
