package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class GroupsViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface {

    private final MutableLiveData<ArrayList<Group>> mGroups;
    private final MutableLiveData<String> mToast;

    public GroupsViewModel(Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeGroupObserver(this);
        mToast = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
    }

    public MutableLiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String t) {
        mToast.setValue(t);
    }

    @Override
    public void update(ArrayList<Group> groups) {
        mGroups.setValue(groups);
    }
}