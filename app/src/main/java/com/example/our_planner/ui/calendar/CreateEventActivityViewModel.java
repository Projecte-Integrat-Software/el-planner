package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface {
    private final MutableLiveData<ArrayList<Group>> mGroups;

    public CreateEventActivityViewModel(@NonNull Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<Group>());
        DataBaseAdapter.subscribeGroupObserver(this);

    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
    }


    public void createEvent(String name, String location, boolean allDay, String date, String startTime, String endTime, String groupId) {
        DataBaseAdapter.createEvent(name, location, allDay, date, startTime, endTime, groupId);
    }

    public void editEvent() {

    }

    public ArrayList<String> getGroupsNames() {
        return DataBaseAdapter.getGroupsNames();
    }

    public Group getGroup(String groupName) {
        return DataBaseAdapter.getGroup(groupName);
    }

    @Override
    public void update(ArrayList<Group> groups) {
        this.mGroups.setValue(groups);
    }

    public List<Group> getGroupsList() {
        return mGroups.getValue();
    }
}
