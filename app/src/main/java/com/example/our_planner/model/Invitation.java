package com.example.our_planner.model;

public class Invitation {
    private String groupId;
    private String title;
    private String author;

    public Invitation(String groupId, String title, String author) {
        this.groupId = groupId;
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGroupId() {
        return groupId;
    }
}
