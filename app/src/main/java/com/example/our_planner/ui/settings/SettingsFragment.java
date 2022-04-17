package com.example.our_planner.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.R;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner notificationsAlertSpinner, themeSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        notificationsAlertSpinner = view.findViewById(R.id.notificationsAlertSpinner);
        ArrayAdapter<String> adapterNotificationsAlert = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.notificationsAlterOptions));
        adapterNotificationsAlert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationsAlertSpinner.setAdapter(adapterNotificationsAlert);
        notificationsAlertSpinner.setSelection(0,false);
        notificationsAlertSpinner.setOnItemSelectedListener(this);

        themeSpinner = view.findViewById(R.id.themeSpinner);
        ArrayAdapter<String> adapterTheme = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.themeOptions));
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapterTheme);
        themeSpinner.setSelection(0,false);
        themeSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}