package com.example.our_planner.model;

import android.widget.ImageView;

public class Invitation {
    private String title;
    private String author;
    private ImageView image;

    public Invitation(String title, String author, ImageView image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
