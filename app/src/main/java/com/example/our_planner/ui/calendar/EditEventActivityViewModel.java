package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.our_planner.model.Event;

public class EditEventActivityViewModel extends AndroidViewModel {

    private Event event;

    public EditEventActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public Event getEvent() {
        return event;
    }

    public void editEvent() {
        //  DataBaseAdapter.editEvent();
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
