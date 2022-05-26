package com.example.our_planner.ui.calendar;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EditEventActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface, DataBaseAdapter.UriInterface {

    private final MutableLiveData<ArrayList<Group>> mGroups;
    private final MutableLiveData<ArrayList<Uri>> mUris;

    private Event event;
    private Group group;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String location;

    public EditEventActivityViewModel(@NonNull Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<>());
        mUris = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeGroupObserver(this);
    }

    public void subscribeUriObserver() {
        DataBaseAdapter.subscribeUriObserver(this, event.getId());
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public void editEvent(String eventId, String name, String location, String date, String startTime, String endTime, String groupId) {
        DataBaseAdapter.editEvent(eventId, name, location, date, startTime, endTime, groupId);
    }

    @Override
    public void updateGroups(ArrayList<Group> groups) {
        this.mGroups.setValue(groups);
    }

    @Override
    public void updateUris(ArrayList<Uri> uris) {
        this.mUris.setValue(uris);
    }

    public MutableLiveData<ArrayList<Uri>> getUris() {
        return mUris;
    }
}
