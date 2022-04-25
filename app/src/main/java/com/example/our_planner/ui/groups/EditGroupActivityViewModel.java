package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;

public class EditGroupActivityViewModel extends AndroidViewModel implements DataBaseAdapter.GroupInterface {

    private final MutableLiveData<String> mToast;
    private final MutableLiveData<Group> mGroup;

    public EditGroupActivityViewModel(Application application) {
        super(application);
        mToast = new MutableLiveData<>();
        mGroup = new MutableLiveData<>();
    }

    public void editGroup(String id, String title, String details, int colour) {
        DataBaseAdapter.editGroup(this, id, title, details, colour);
    }

    public MutableLiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String s) {
        mToast.setValue(s);
    }

    public MutableLiveData<Group> getGroup() {
        return mGroup;
    }

    @Override
    public void setGroup(Group g) {
        mGroup.setValue(g);
    }
}
