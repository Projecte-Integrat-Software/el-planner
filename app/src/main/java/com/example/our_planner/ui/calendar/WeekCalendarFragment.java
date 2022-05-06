package com.example.our_planner.ui.calendar;

import static com.example.our_planner.ui.calendar.CalendarUtils.daysInWeekArray;
import static com.example.our_planner.ui.calendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;


public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{

    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView recyclerViewEvents;

    private Button previousWeekBtn;
    private Button nextWeekBtn;
    private Button newEventBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week_calendar, container, false);

        initWidgets();

        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initListeners();
    }

    private void initWidgets()
    {
        monthYearText = view.findViewById(R.id.monthYearTV);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        previousWeekBtn = view.findViewById(R.id.previousWeekBtn);
        nextWeekBtn = view.findViewById(R.id.nextWeekBtn);
        newEventBtn = view.findViewById(R.id.newEventBtn);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListeners() {
        previousWeekBtn.setOnClickListener(this::previousWeekAction);
        nextWeekBtn.setOnClickListener(this::nextWeekAction);
        newEventBtn.setOnClickListener(this::newEventAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this.getActivity(), EditEventActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter adapter = new EventAdapter(getContext(), dailyEvents);
        recyclerViewEvents.setAdapter(adapter);
    }
}