/*package com.example.our_planner.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarGroupsFragment extends Fragment {

    private ArrayList<Group> groups;
    private RecyclerView recyclerViewCalendarGroups;
    static final int COLUMNS = 3;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_groups, container, false);

        recyclerViewCalendarGroups = view.findViewById(R.id.recyclerViewCalendarGroups);
        recyclerViewCalendarGroups.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS));

        // For testing purposes
        HashMap m = new HashMap();
        groups.add(new Group("", "PIS", "Theory", m, m, m));
        groups.add(new Group("", "Geometry", "Problems", m, m, m));
        groups.add(new Group("", "PAE", "Labs", m, m, m));

        AdapterCalendarGroups adapterCalendarGroups = new AdapterCalendarGroups(groups);
        recyclerViewCalendarGroups.setAdapter(adapterCalendarGroups);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
*/