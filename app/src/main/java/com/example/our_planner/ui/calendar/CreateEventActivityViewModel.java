package com.example.our_planner.ui.calendar;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface, DataBaseAdapter.UriInterface {
    private final MutableLiveData<ArrayList<Group>> mGroups;
    private final MutableLiveData<ArrayList<Uri>> mUris;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public CreateEventActivityViewModel(@NonNull Application application) {
        super(application);
        mGroups = new MutableLiveData<>(new ArrayList<>());
        mUris = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeGroupObserver(this);

    }

    public MutableLiveData<ArrayList<Group>> getGroups() {
        return mGroups;
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

    public Task<DocumentReference> createEvent(String name, String location, String date, String startTime, String endTime, String groupId) {
        return DataBaseAdapter.createEvent(name, location, date, startTime, endTime, groupId);
    }

    @Override
    public void updateGroups(ArrayList<Group> groups) {
        this.mGroups.setValue(groups);
    }

    public List<Group> getGroupsList() {
        return mGroups.getValue();
    }

    @Override
    public void updateUris(ArrayList<Uri> uris) {
        this.mUris.setValue(new ArrayList<>(uris));
    }

    public void subscribeUriObserver() {
        DataBaseAdapter.subscribeUriObserverCreate(this);
    }

    public MutableLiveData<ArrayList<Uri>> getUris() {
        return mUris;
    }

    public void addUri(Uri uri) {
        ArrayList<Uri> uris = mUris.getValue();
        uris.add(uri);
        mUris.setValue(uris);
    }
}
