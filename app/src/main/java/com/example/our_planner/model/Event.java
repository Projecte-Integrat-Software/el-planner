package com.example.our_planner.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Serializable {

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
    private boolean allDay;
    private LocalDate date;
    private LocalTime time;

    public Event(String id, String name, String location, Boolean allDay, LocalDate date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.allDay = allDay;
        this.date = date;
        this.time = time;
    }

    public Event(String id, String name, String location, Boolean allDay) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.allDay = allDay;
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

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
