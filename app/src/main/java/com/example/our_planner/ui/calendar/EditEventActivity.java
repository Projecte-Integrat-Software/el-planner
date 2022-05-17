package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;
import com.example.our_planner.ui.calendar.comments.CommentsActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EditEventActivity extends AppCompatActivity {
    private EditEventActivityViewModel viewModel;

    private EditText eventNameET;
    private TextView dateTV, startTimeTV, endTimeTV;
    private Button selectDateBtn, selectStartTimeBtn, selectEndTimeBtn, createBtn, commentEventBtn;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private androidx.appcompat.app.AlertDialog alert;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(EditEventActivityViewModel.class);
        viewModel.setDate(CalendarUtils.selectedDate);
        LocalTime startTime = LocalTime.now();
        viewModel.setStartTime(startTime.minusSeconds(startTime.getSecond()));
        viewModel.setEndTime(startTime.plusHours(1));

        initAlarmDialog();
        initWidgets();
        initDatePicker();
        initStartTimePicker();
        initEndTimePicker();
        initListeners();


        viewModel.setEvent((Event) getIntent().getSerializableExtra("event"));
        dateTV.setText(viewModel.getEvent().getName());
    }

    private void initAlarmDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        eventNameET = findViewById(R.id.txtEventTitle);

        dateTV = findViewById(R.id.dateTV);
        startTimeTV = findViewById(R.id.startTimeTV);
        endTimeTV = findViewById(R.id.endTimeTV);

        selectDateBtn = findViewById(R.id.selectDateBtn);
        selectStartTimeBtn = findViewById(R.id.selectStartTimeBtn);
        selectEndTimeBtn = findViewById(R.id.selectEndTimeBtn);
        createBtn = findViewById(R.id.btnCreate);
        commentEventBtn = findViewById(R.id.btnCommentEvent);

        dateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        startTimeTV.setText(CalendarUtils.formattedTime(viewModel.getStartTime()));
        endTimeTV.setText(CalendarUtils.formattedTime(viewModel.getEndTime()));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            viewModel.setDate(LocalDate.of(year, month, day));
            dateTV.setText(CalendarUtils.formattedDate(viewModel.getDate()));
        };

        int year = viewModel.getDate().getYear();
        int month = viewModel.getDate().getMonthValue();
        int day = viewModel.getDate().getDayOfMonth();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initStartTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            viewModel.setStartTime(LocalTime.of(selectedHour, selectedMinute));
            startTimeTV.setText(CalendarUtils.formattedTime(viewModel.getStartTime()));
        };

        int hour = viewModel.getStartTime().getHour();
        int minute = viewModel.getStartTime().getHour();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        startTimePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
    }

    private void initEndTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            viewModel.setEndTime(LocalTime.of(selectedHour, selectedMinute));
            endTimeTV.setText(CalendarUtils.formattedTime(viewModel.getEndTime()));
        };

        int hour = viewModel.getEndTime().getHour();
        int minute = viewModel.getEndTime().getMinute();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        endTimePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
    }

    private void initListeners() {
        selectDateBtn.setOnClickListener(this::openDatePicker);
        selectStartTimeBtn.setOnClickListener(this::openStartTimePicker);
        selectEndTimeBtn.setOnClickListener(this::openEndTimePicker);
        createBtn.setOnClickListener(this::saveEventAction);
        commentEventBtn.setOnClickListener(this::commentEventAction);
    }

    private void commentEventAction(View view) {
        Intent intent = new Intent(view.getContext(), CommentsActivity.class);
        intent.putExtra("idEvent", viewModel.getEvent().getId());
        startActivity(intent);
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void openStartTimePicker(View view) {
        startTimePickerDialog.show();
    }

    public void openEndTimePicker(View view) {
        endTimePickerDialog.show();
    }

    private void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        // TODO Implement IDs in events
        LocalTime time1 = viewModel.getStartTime();
        LocalTime time2 = viewModel.getEndTime();
        LocalDate date = CalendarUtils.selectedDate;
        Event newEvent = new Event(eventName, eventName, "location", false, date, time1, time2, null);
        Event.eventsList.add(newEvent);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

//        viewModel.createEvent(newEvent.getId(), newEvent.getName(), newEvent.getLocation(), newEvent.isAllDay(), date.format(formatter), newEvent.getStartTime().toString(), newEvent.getEndTime().toString());

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help) {
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}