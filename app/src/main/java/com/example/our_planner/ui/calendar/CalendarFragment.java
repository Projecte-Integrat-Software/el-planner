package com.example.our_planner.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.R;

public class CalendarFragment extends Fragment {

//    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        Spinner calendarSelectorSpinner = view.findViewById(R.id.calendarSelectorSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(CalendarFragment.this.getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.calendarOptions));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendarSelectorSpinner.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}