package com.example.our_planner.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class CalendarGroupsFragment extends Fragment {

    private ArrayList<Group> groups;
    private RecyclerView recyclerViewCalendarGroups;
    static final int COLUMNS = 3;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_groups, container, false);

        recyclerViewCalendarGroups = view.findViewById(R.id.recyclerViewCalendarGroups);
        recyclerViewCalendarGroups.setLayoutManager(new GridLayoutManager(getContext(),COLUMNS));

        // ArrayList for testing purposes
        groups.add(new Group("", "PIS", "Theory", 0));
        groups.add(new Group("", "Geometry", "Problems", 0xfe0037));
        groups.add(new Group("", "PAE", "Labs", 0x33cc33));

        AdapterCalendarGroups adapterCalendarGroups = new AdapterCalendarGroups(groups);
        recyclerViewCalendarGroups.setAdapter(adapterCalendarGroups);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
