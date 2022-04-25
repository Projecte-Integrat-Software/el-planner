package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class GroupsViewModel extends AndroidViewModel {

    private final ArrayList<Group> groups;

    public GroupsViewModel(Application application) {
        super(application);
        groups = new ArrayList<>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group g) {
        groups.add(g);
    }

    public void editGroup(Group g, int i) {
        groups.set(i, g);
    }
}