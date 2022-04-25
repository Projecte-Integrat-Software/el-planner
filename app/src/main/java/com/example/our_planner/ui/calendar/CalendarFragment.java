package com.example.our_planner.ui.calendar;

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
import androidx.fragment.app.FragmentTransaction;

import com.example.our_planner.R;
import com.example.our_planner.ui.calendar.comments.CommentsActivity;

public class CalendarFragment extends Fragment {

    Spinner spinner;
    Button commentEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        spinner = view.findViewById(R.id.calendarSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.calendarOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        commentEvent = view.findViewById(R.id.commentEvent);

        commentEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CommentsActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        insertNestedFragment();
        initListeners();
    }

    private void insertNestedFragment() {
        Fragment childFragment = new MonthCalendarFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    private void initListeners() {
//        spinner.setOnItemClickListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}