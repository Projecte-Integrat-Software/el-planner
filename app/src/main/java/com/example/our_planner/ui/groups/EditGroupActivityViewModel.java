package com.example.our_planner.ui.groups;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Group;
import com.example.our_planner.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditGroupActivityViewModel extends AndroidViewModel implements DataBaseAdapter.EmailCheckerInterface {

    private final MutableLiveData<String> mToast;
    private ArrayList<String> invitationEmails;
    private final String email;
    private Map<String, Integer> colours;
    private MutableLiveData<Boolean> mRegistered;

    public EditGroupActivityViewModel(Application application) {
        super(application);
        mToast = new MutableLiveData<>();
        invitationEmails = new ArrayList<>();
        colours = new HashMap<>();
        email = DataBaseAdapter.getEmail();
    }

    public void editGroup(String id, String title, String details, int colour, Map<String, User> participants, Map<String, Boolean> admins) {
        colours.replace(email, colour);
        //Update colours map with expelled users
        for (String k : colours.keySet()) {
            if (!participants.containsKey(k)) {
                colours.remove(k);
            }
        }
        DataBaseAdapter.editGroup(this, id, title, details, colours, participants, admins, invitationEmails);
    }

    public MutableLiveData<String> getToast() {
        return mToast;
    }

    public MutableLiveData<Boolean> getRegistered() {
        return mRegistered;
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

    public int getColour(Group g) {
        colours = g.getColours();
        return colours.get(email);
    }

    public String getUserName() {
        return DataBaseAdapter.getUserName();
    }

    public String getEmail() {
        return DataBaseAdapter.getEmail();
    }

    public void resetRegistered() {
        mRegistered = new MutableLiveData<>();
    }

    public void checkRegistered(String email) {
        DataBaseAdapter.checkRegistered(this, email);
    }

    @Override
    public void setChecked(boolean b) {
        mRegistered.setValue(b);
    }
}
