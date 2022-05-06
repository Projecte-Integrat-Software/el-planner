package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV, dateTV;
    private Button selectDateBtn, createBtn;
    private DatePickerDialog datePickerDialog;

    private LocalDate date;
    private LocalTime time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        time = LocalTime.now();
        initWidgets();
        initDatePicker();
        initListeners();
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        eventNameET = findViewById(R.id.txtEventTitle);
        eventDateTV = findViewById(R.id.txtEventStart);
        eventTimeTV = findViewById(R.id.txtEventEnd);
        dateTV = findViewById(R.id.dateTV);
        selectDateBtn = findViewById(R.id.selectDateBtn);
        createBtn = findViewById(R.id.btnCreate);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        dateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateTV.setText(date);
        };

        int year = CalendarUtils.selectedDate.getYear();
        int month = CalendarUtils.selectedDate.getMonthValue();
        int day = CalendarUtils.selectedDate.getDayOfMonth();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        date = LocalDate.of(year, month, day);
        return CalendarUtils.formattedDate(date);
    }

    private void initListeners() {
        selectDateBtn.setOnClickListener(this::openDatePicker);
        createBtn.setOnClickListener(this::saveEventAction);
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }

}
