package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;

public class CreateGroupActivityViewModel extends AndroidViewModel implements DataBaseAdapter.DBInterface {

    private final MutableLiveData<String> mToast;

    public CreateGroupActivityViewModel(Application application) {
        super(application);
        mToast = new MutableLiveData<>();
    }

    public void createGroup(String title, String details, int colour) {
        DataBaseAdapter.createGroup(this, title, details, colour);
    }

    public MutableLiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String s) {
        mToast.setValue(s);
    }
}
