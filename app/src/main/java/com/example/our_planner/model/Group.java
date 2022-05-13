package com.example.our_planner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Group implements Serializable {
    private final String id;
    private String title;
    private String details;
    private final Map<String, Integer> colours;
    private final Map<String, User> participants;
    private final Map<String, Boolean> admins;
    private final ArrayList<Event> events;

    public Group(String id, String title, String details, Map<String, Integer> colours, Map<String, User> participants, Map<String, Boolean> admins) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.colours = colours;
        this.participants = participants;
        this.admins = admins;
        this.events = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Map<String, User> getParticipants() {
        return participants;
    }

    public Map<String, Boolean> getAdmins() {
        return admins;
    }

    public Map<String, Integer> getColours() {
        return colours;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    @Override
    public String toString() {
        return title;
    }
}
