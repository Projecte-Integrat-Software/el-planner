package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EditEventActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface {

    private final MutableLiveData<ArrayList<Group>> mGroups;

    private Event event;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String location;

    public EditEventActivityViewModel(@NonNull Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeGroupObserver(this);
    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return event.getId();
    }

    public void updateData() {
        date = event.getDate();
        startTime = event.getStartTime();
        endTime = event.getEndTime();
        location = event.getLocation();
    }

    public void editEvent(String eventId, String name, String location, boolean allDay, String date, String startTime, String endTime, String group) {
        DataBaseAdapter.editEvent(eventId, name, location, allDay, date, startTime, endTime, group);
    }

    @Override
    public void update(ArrayList<Group> groups) {
        this.mGroups.setValue(groups);
    }
}
