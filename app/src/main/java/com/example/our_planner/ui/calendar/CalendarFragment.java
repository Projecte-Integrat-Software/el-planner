package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.our_planner.R;
import com.example.our_planner.ui.calendar.comments.ShowCommentsActivity;

public class CalendarFragment extends Fragment {

    Button commentBtn1;
    Button commentBtn2;

    @SuppressLint("CutPasteId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        Spinner calendarSelectorSpinner = view.findViewById(R.id.calendarSelectorSpinner);
        commentBtn1 = view.findViewById(R.id.commentBtn1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(CalendarFragment.this.getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.calendarOptions));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendarSelectorSpinner.setAdapter(myAdapter);

        commentBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ShowCommentsActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}