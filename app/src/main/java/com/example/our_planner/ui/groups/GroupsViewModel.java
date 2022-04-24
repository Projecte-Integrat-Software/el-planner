package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class GroupsViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<Group>> mGroups;

    public GroupsViewModel(Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
    }

    public void addGroup(Group g) {
        mGroups.getValue().add(g);
    }
}