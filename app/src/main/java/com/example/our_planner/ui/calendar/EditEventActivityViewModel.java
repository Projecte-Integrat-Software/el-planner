package com.example.our_planner.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.our_planner.model.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EditEventActivityViewModel extends AndroidViewModel {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Event event;

    public EditEventActivityViewModel(@NonNull Application application) {
        super(application);
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

    public void editEvent() {
        //  DataBaseAdapter.editEvent();
    }
}
