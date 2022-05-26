package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface {
    private final MutableLiveData<ArrayList<Group>> mGroups;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public CreateEventActivityViewModel(@NonNull Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<Group>());
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

    public void createEvent(String name, String location, boolean allDay, String date, String startTime, String endTime, String groupId) {
        DataBaseAdapter.createEvent(name, location, allDay, date, startTime, endTime, groupId);
    }

    @Override
    public void updateGroups(ArrayList<Group> groups) {
        this.mGroups.setValue(groups);
    }

    public List<Group> getGroupsList() {
        return mGroups.getValue();
    }
}
