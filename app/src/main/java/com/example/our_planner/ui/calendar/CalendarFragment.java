package com.example.our_planner.ui.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CalendarFragment extends Fragment implements AdapterCalendarGroups.OnGroupListener {

    private Spinner spinner;
    private ImageButton calendarGroups;
    private ArrayList<Group> groups;
    private CalendarViewModel calendarViewModel;
    private RecyclerView recyclerCalendarGroups;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        Resources r = LocaleLanguage.getLocale(getContext()).getResources();
        spinner = view.findViewById(R.id.calendarSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, r.getStringArray(R.array.calendarOptions));
        adapter.setDropDownViewResource(
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //      AdapterCalendarGroups adapterGroups = new AdapterCalendarGroups(new ArrayList<Group>());
        //      recyclerCalendarGroups.setAdapter(adapterGroups);


        groups = new ArrayList<>();
        AtomicReference<AdapterCalendarGroups> adapterGroups = new AtomicReference<>(new AdapterCalendarGroups(groups, this, CalendarViewModel.mSelections.getValue()));

/*        HashMap m = new HashMap();
        groups.add(new Group("", "PIS", "Theory", m, m, m));
        groups.add(new Group("", "Geometry", "Problems", m, m, m));
        groups.add(new Group("", "PAE", "Labs", m, m, m));
*/
        calendarGroups = view.findViewById(R.id.btnCalendarGroups);
        calendarGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_calendar_groups, null, false), 600, 800, true);
                //pw.showAtLocation(button, Gravity.CENTER, 0, 0);
                pw.showAsDropDown(calendarGroups, 0, 0);

                recyclerCalendarGroups = pw.getContentView().findViewById(R.id.recyclerViewCalendarGroups);
                recyclerCalendarGroups.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                recyclerCalendarGroups.setAdapter(adapterGroups.get());
            }


        });

        Observer<ArrayList<Group>> observerGroups = i -> {
            //        AdapterCalendarGroups newAdapter = new AdapterCalendarGroups(i);
            //        recyclerCalendarGroups.swapAdapter(newAdapter, false);
            //        newAdapter.notifyDataSetChanged();
            adapterGroups.set(new AdapterCalendarGroups(i, this, CalendarViewModel.mSelections.getValue()));
        };
        calendarViewModel.getGroups().observe(getActivity(), observerGroups);

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onGroupSelect(Map<String, Boolean> selections) {
        calendarViewModel.setSelections(selections);
    }
}