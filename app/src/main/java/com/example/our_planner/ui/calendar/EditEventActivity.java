package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;

import java.time.LocalTime;

public class EditEventActivity extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button saveBtn;
    private Button cancelBtn;

    private LocalTime time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

        initListeners();
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.txtEventTitle);
        eventDateTV = findViewById(R.id.txtEventStart);
        eventTimeTV = findViewById(R.id.txtEventEnd);
        saveBtn = findViewById(R.id.btnEdit);
        cancelBtn = findViewById(R.id.btnCancel);

        cancelBtn.setOnClickListener(view -> finish());
    }

    private void initListeners() {
        saveBtn.setOnClickListener(this::saveEventAction);
    }

    private void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event("idTest", eventName, "location", false, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }

}