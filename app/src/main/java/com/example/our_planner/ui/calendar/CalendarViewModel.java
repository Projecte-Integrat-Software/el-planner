package com.example.our_planner.ui.calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class CalendarViewModel extends ViewModel implements DataBaseAdapter.GroupInterface{

    private final MutableLiveData<ArrayList<Group>> mGroups;
    //private final MutableLiveData<String> mToast;

    public CalendarViewModel() {
        super();
        mGroups = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeGroupObserver(this);
    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
    }

    /*
    public LiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String s) {

    }
*/
    @Override
    public void update(ArrayList<Group> groups) {
        mGroups.setValue(groups);
    }
}