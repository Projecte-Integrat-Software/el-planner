package com.example.our_planner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Serializable, Comparable {

    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    private final String id;
    private String name;
    private String location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String groupId;

    public Event(String id, String name, String location, LocalDate date, LocalTime startTime, LocalTime endTime, String groupId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupId = groupId;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static void updateEventsList(ArrayList<Event> events) {
        eventsList = events;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public int compareTo(Object o) {
        Event prov = (Event) o;
        if (this.getStartTime().isBefore(((Event) o).getStartTime())) {
            return -1;
        } else {
            return 1;
        }
    }
}
