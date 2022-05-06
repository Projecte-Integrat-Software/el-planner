package com.example.our_planner.ui.calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Event;

import java.util.ArrayList;

public class EventsViewModel extends ViewModel implements DataBaseAdapter.EventInterface {

    private final MutableLiveData<ArrayList<Event>> events;


    public EventsViewModel() {
        events = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeEventsObserver(this);

    }

    public MutableLiveData<ArrayList<Event>> getEvents() {
        return events;
    }

    @Override
    public void update(ArrayList<Event> events) {
        //   Event.eventsList = events;
        this.events.setValue(events);
    }
}