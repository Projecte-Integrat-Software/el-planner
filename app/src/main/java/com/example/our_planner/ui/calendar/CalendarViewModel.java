package com.example.our_planner.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class CalendarViewModel extends ViewModel implements DataBaseAdapter.CalendarGroupInterface{

    private final MutableLiveData<String> mText;

    public CalendarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calendar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void setToast(String s) {

    }

    @Override
    public void update(ArrayList<Group> groups) {

    }
}