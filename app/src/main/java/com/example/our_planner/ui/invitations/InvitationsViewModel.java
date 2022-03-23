package com.example.our_planner.ui.invitations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InvitationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InvitationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is invitations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}