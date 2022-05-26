package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {
    private CreateEventActivityViewModel viewModel;

    private EditText eventNameET, eventLocationET;
    private TextView dateTV, startTimeTV, endTimeTV;
    private Button selectDateBtn, selectStartTimeBtn, selectEndTimeBtn, createBtn;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;
    private Spinner selectGroup;

    private Group group;
    private ArrayList<Group> groups;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private androidx.appcompat.app.AlertDialog alert;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(CreateEventActivityViewModel.class);
        date = CalendarUtils.selectedDate;
        startTime = LocalTime.now();
        startTime = startTime.minusSeconds(startTime.getSecond());
        endTime = startTime.plusHours(1);

        viewModel = new ViewModelProvider(this).get(CreateEventActivityViewModel.class);
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

        selectGroup = findViewById(R.id.selectGroupSpinner);


        Observer<ArrayList<Group>> observerGroups = i -> {
            //        AdapterCalendarGroups newAdapter = new AdapterCalendarGroups(i);
            //        recyclerCalendarGroups.swapAdapter(newAdapter, false);
            //        newAdapter.notifyDataSetChanged();
            Iterator<Group> it = i.iterator();
            List<String> groups2 = new ArrayList<>();


            while (it.hasNext()) {
                String temp = it.next().getTitle();
                groups2.add(temp);
            }

            groups = i;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectGroup.setAdapter(adapter);
        };
        viewModel.getGroups().observe(this, observerGroups);


        selectGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                group = groups.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initAlarmDialog() {
        Resources r = LocaleLanguage.getLocale(this).getResources();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.help_create_event));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        eventNameET = findViewById(R.id.txtEventTitle);
        eventLocationET = findViewById(R.id.eventLocationET);

        dateTV = findViewById(R.id.dateTV);
        startTimeTV = findViewById(R.id.startTimeTV);
        endTimeTV = findViewById(R.id.endTimeTV);


        //       ArrayAdapter groupAdapter = new ArrayAdapter(this, R.layout.groups_spinner, groups);

        //     selectGroup.setAdapter(groupAdapter);


        //      adapter.setDropDownViewResource(
        //              androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        selectDateBtn = findViewById(R.id.selectDateBtn);
        selectStartTimeBtn = findViewById(R.id.selectStartTimeBtn);
        selectEndTimeBtn = findViewById(R.id.selectEndTimeBtn);
        createBtn = findViewById(R.id.btnCreate);


        dateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        startTimeTV.setText(CalendarUtils.formattedTime(startTime));
        endTimeTV.setText(CalendarUtils.formattedTime(endTime));
       /* eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(startTime)); */
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            viewModel.setDate(LocalDate.of(year, month, day));
            dateTV.setText(CalendarUtils.formattedDate(viewModel.getDate()));
        };

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initStartTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            viewModel.setStartTime(LocalTime.of(selectedHour, selectedMinute));
            startTimeTV.setText(CalendarUtils.formattedTime(viewModel.getStartTime()));
        };

        int hour = startTime.getHour();
        int minute = startTime.getMinute();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        startTimePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
    }

    private void initEndTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            viewModel.setEndTime(LocalTime.of(selectedHour, selectedMinute));
            endTimeTV.setText(CalendarUtils.formattedTime(viewModel.getEndTime()));
        };

        int hour = endTime.getHour();
        int minute = endTime.getMinute();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        endTimePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
    }

    private void initListeners() {
        selectDateBtn.setOnClickListener(this::openDatePicker);
        selectStartTimeBtn.setOnClickListener(this::openStartTimePicker);
        selectEndTimeBtn.setOnClickListener(this::openEndTimePicker);
        createBtn.setOnClickListener(this::saveEventAction);
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

        if (eventName.equals("")) {
            Resources r = LocaleLanguage.getLocale(this).getResources();
            Toast.makeText(this, r.getString(R.string.title_empty), Toast.LENGTH_SHORT).show();
        } else {
            String location = eventLocationET.getText().toString();
            // TODO Fix spinner and get group from there
            // Delete line above and uncomment line below when spinner fixed
            //group = (Group) selectGroup.getSelectedItem();
            LocalTime startTime = viewModel.getStartTime();
            LocalTime endTime = viewModel.getEndTime();
            LocalDate date = viewModel.getDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            viewModel.createEvent(eventName, location, date.format(formatter), startTime.toString(), endTime.toString(), group.getId());

            finish();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
    }

    private void changeLanguage() {
        Resources r = LocaleLanguage.getLocale(this).getResources();

        setTitle(r.getString(R.string.title_activity_create_event));
        ((TextView) findViewById(R.id.labelEventTitle)).setText(r.getString(R.string.title));
        eventNameET.setHint(r.getString(R.string.event_name));
        ((TextView) findViewById(R.id.labelEventLocation)).setText(r.getString(R.string.location));
        eventLocationET.setHint(r.getString(R.string.location));
        selectDateBtn.setText(r.getString(R.string.select_date));
        selectStartTimeBtn.setText(r.getString(R.string.select_start_time));
        selectEndTimeBtn.setText(r.getString(R.string.select_end_time));
        createBtn.setText(r.getString(R.string.create));
        ((TextView) findViewById(R.id.labelSelectGroup)).setText(r.getString(R.string.select_group));

    }
}
