package com.example.our_planner.ui.invitations;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Invitation;

import java.util.ArrayList;

public class InvitationsViewModel extends ViewModel implements DataBaseAdapter.InvitationInterface {

    private final MutableLiveData<ArrayList<Invitation>> mInvitations;
    private final MutableLiveData<String> mToast;

    public InvitationsViewModel() {
        mInvitations = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.subscribeInvitationObserver(this);
        mToast = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Invitation>> getInvitations() {
        return mInvitations;
    }

    public MutableLiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String t) {
        mToast.setValue(t);
    }

    @Override
    public void update(ArrayList<Invitation> invitations) {
        mInvitations.setValue(invitations);
    }
}