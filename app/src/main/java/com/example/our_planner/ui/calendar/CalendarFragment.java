package com.example.our_planner.ui.calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.example.our_planner.ui.calendar.comments.CommentsActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarFragment extends Fragment {

    private Spinner spinner;
    private Button commentEvent;
    private ImageButton calendarGroups;
    private ArrayList<Group> groups;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        spinner = view.findViewById(R.id.calendarSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.calendarOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        groups = new ArrayList<>();
        HashMap m = new HashMap();
        groups.add(new Group("", "PIS", "Theory", m, m, m));
        groups.add(new Group("", "Geometry", "Problems", m, m, m));
        groups.add(new Group("", "PAE", "Labs", m, m, m));

        commentEvent = view.findViewById(R.id.commentEvent);
        calendarGroups = view.findViewById(R.id.btnCalendarGroups);
        calendarGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arreglar la següent línia
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_calendar_groups, null, false), 600, 800, true);
                //pw.showAtLocation(button, Gravity.CENTER, 0, 0);
                pw.showAsDropDown(calendarGroups, 0, 0);

                RecyclerView recyclerView = pw.getContentView().findViewById(R.id.recyclerViewCalendarGroups);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

                AdapterCalendarGroups adapter = new AdapterCalendarGroups(groups);
                recyclerView.setAdapter(adapter);
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
        Fragment childFragment = new WeekCalendarFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    private void initListeners() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mode = spinner.getSelectedItem().toString();
                Fragment childFragment;
                FragmentTransaction transaction;
                switch (mode) {
                    case "Week":
                        childFragment = new WeekCalendarFragment();
                        transaction = getChildFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.child_fragment_container, childFragment).commit();
                        break;
                    case "Month":
                        childFragment = new MonthCalendarFragment();
                        transaction = getChildFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.child_fragment_container, childFragment).commit();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        commentEvent.setOnClickListener(view -> startActivity(new Intent(view.getContext(),
                CommentsActivity.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}