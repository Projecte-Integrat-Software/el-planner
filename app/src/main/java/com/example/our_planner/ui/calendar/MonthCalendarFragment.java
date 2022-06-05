package com.example.our_planner.ui.calendar;

import static com.example.our_planner.ui.calendar.CalendarUtils.daysInMonthArray;
import static com.example.our_planner.ui.calendar.CalendarUtils.monthYearFromDate;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.ThemeSwitcher;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{

    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private Button previousMonthBtn;
    private Button nextMonthBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        initWidgets();

        Bundle arguments = getArguments();
        if (arguments == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        } else {
            int day = arguments.getInt("day");
            int month = arguments.getInt("month");
            int year = arguments.getInt("year");
            CalendarUtils.selectedDate = LocalDate.of(year, month, day);
        }

        setMonthView();

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

        previousMonthBtn = view.findViewById(R.id.previousMonthBtn);
        nextMonthBtn = view.findViewById(R.id.nextMonthBtn);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListeners() {
        previousMonthBtn.setOnClickListener(this::previousMonthAction);
        nextMonthBtn.setOnClickListener(this::nextMonthAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        monthYearText.setTextColor(getResources().getColor(ThemeSwitcher.lightThemeSelected() ? R.color.black : R.color.white));
        previousMonthBtn.setTextColor(getResources().getColor(ThemeSwitcher.lightThemeSelected() ? R.color.black : R.color.white));
        nextMonthBtn.setTextColor(getResources().getColor(ThemeSwitcher.lightThemeSelected() ? R.color.black : R.color.white));


        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
}