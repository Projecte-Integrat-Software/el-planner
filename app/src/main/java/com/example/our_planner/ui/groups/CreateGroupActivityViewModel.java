package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;

import java.util.ArrayList;

public class CreateGroupActivityViewModel extends AndroidViewModel implements DataBaseAdapter.DBInterface {

    private final MutableLiveData<String> mToast;
    private ArrayList<String> invitationEmails;

    public CreateGroupActivityViewModel(Application application) {
        super(application);
        mToast = new MutableLiveData<>();
        invitationEmails = new ArrayList<>();
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

    public ArrayList<String> getInvitationEmails() {
        return invitationEmails;
    }

    public void saveInvitationEmails(ArrayList<String> invitationEmails) {
        this.invitationEmails = invitationEmails;
    }
}
