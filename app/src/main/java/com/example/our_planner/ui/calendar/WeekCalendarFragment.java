package com.example.our_planner.ui.calendar;

import static com.example.our_planner.ui.calendar.CalendarUtils.daysInWeekArray;
import static com.example.our_planner.ui.calendar.CalendarUtils.monthYearFromDate;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.ThemeSwitcher;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener, EventAdapter.OnNoteListener {

    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView recyclerViewEvents;

    private Button previousWeekBtn;
    private Button nextWeekBtn;
    private Button newEventBtn;

    private CalendarViewModel calendarViewModel;


    private Map<String, Boolean> selections;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week_calendar, container, false);
        selections = new HashMap<>();
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        initWidgets();


        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();
        setEventAdapter();


        Observer<Map<String, Boolean>> observerSelections = i -> {
            selections = i;
            setEventAdapter();
        };
        calendarViewModel.getSelections().observe(getActivity(), observerSelections);



     /*   Observer<ArrayList<Group>> observerGroups = i -> {

        };
        calendarViewModel.getGroups().observe(getActivity(), observerGroups);
        */
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initListeners();
        setEventAdapter();
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
        monthYearText.setTextColor(getResources().getColor(ThemeSwitcher.lightThemeSelected() ? R.color.black : R.color.white));
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
        startActivity(new Intent(this.getActivity(), CreateEventActivity.class));
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
        setWeekView();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);

        ArrayList<Event> events = new ArrayList<>();

        for (Event e : dailyEvents) {
            if (selections.containsKey(e.getGroupId())) {
                if (selections.get(e.getGroupId()))
                    events.add(e);
            }
        }

        EventAdapter adapter = new EventAdapter(getContext(), events, this, calendarViewModel.getGroups());
        recyclerViewEvents.setAdapter(adapter);

   /*     ArrayList<Event> events = new ArrayList<>();
        // Now we check the events from the groups selected
        // First we need to get the groups selected
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group("3UJ98vjspiEL4LYugkX8", "", "", null, null, null));

        Iterator<Group> iterGroups;
        Iterator<Event> iterEvents = dailyEvents.iterator();
        Event event;
        while (iterEvents.hasNext()) {
            event = (Event) iterEvents.next();
            iterGroups = groups.iterator();
            while (iterGroups.hasNext()) {
                if (event.getGroup().equals(((Group) iterGroups.next()).getId())) {
                    events.add(event);
                    break;
                }

            }
        }
*/
        //   EventAdapter adapter = new EventAdapter(getContext(), dailyEvents, this);
        //   recyclerViewEvents.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(int position, Event event) {
        Context c = view.getContext();
        Intent i = new Intent(c, EditEventActivity.class);
        i.putExtra("event", event);

        //Trobem el grup al qual pertany l'event i el passem
        for (Group g : calendarViewModel.getGroups().getValue()) {
            if (g.getId().equals(event.getGroupId())) {
                i.putExtra("group", g);
            }
        }
        c.startActivity(i);
    }


}