package com.example.our_planner.ui.user;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;


public class LoginActivityViewModel extends AndroidViewModel implements DataBaseAdapter.DBInterface {

    private final MutableLiveData<String> mToast;

    public LoginActivityViewModel(Application application) {
        super(application);
        mToast = new MutableLiveData<>();
    }

    public boolean isUserLogged() {
        return DataBaseAdapter.alreadyLoggedIn();
    }

    public void login(String email, String password) {
        DataBaseAdapter.login(this, email, password);
    }

    public LiveData<String> getToast() {
        return mToast;
    }

    @Override
    public void setToast(String s) {
        mToast.setValue(s);
    }
}
