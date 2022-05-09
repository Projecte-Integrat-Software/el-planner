package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CreateEventActivityViewModel extends AndroidViewModel {

    public CreateEventActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void createEvent(String id, String name, String location, boolean allDay, String date, String startTime, String endTime) {
        //DataBaseAdapter.createEvent(id, name, location, allDay, date, startTime, endTime);
    }

    public void editEvent() {

    }
}
