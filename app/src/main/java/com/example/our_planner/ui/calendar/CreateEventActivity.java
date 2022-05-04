package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.our_planner.R;
import java.time.LocalTime;

public class CreateEventActivity extends AppCompatActivity{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button createBtn;

    private LocalTime time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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
        createBtn = findViewById(R.id.btnCreate);
    }

    private void initListeners() {
        createBtn.setOnClickListener(this::saveEventAction);
    }

    private void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }

}
