package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;
import com.example.our_planner.ui.calendar.comments.CommentsActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class EditEventActivity extends AppCompatActivity {
    private EditEventActivityViewModel viewModel;

    private EditText eventNameET, eventLocationET;
    private TextView dateTV, startTimeTV, endTimeTV;
    private static final int FILE_SELECT_CODE = 0;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;
    private Spinner selectGroup;
    private Button selectDateBtn, selectStartTimeBtn, selectEndTimeBtn, saveChangesBtn, commentEventBtn, addFilesBtn;

    private Group group;
    private ArrayList<Group> groups;
    private RecyclerView fileList;

    private androidx.appcompat.app.AlertDialog alert;
    private ArrayList<Uri> uris;

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
        viewModel.setEvent((Event) getIntent().getSerializableExtra("event"));

        initAlarmDialog();
        initWidgets();
        initDatePicker();
        initStartTimePicker();
        initEndTimePicker();
        fillData();
        initFilesList();
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

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groups2);
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

    private void fillData() {
        eventNameET.setText(viewModel.getEvent().getName());
        viewModel.updateData();
        dateTV.setText(CalendarUtils.formattedDate(viewModel.getDate()));
        startTimeTV.setText(CalendarUtils.formattedTime(viewModel.getStartTime()));
        endTimeTV.setText(CalendarUtils.formattedTime(viewModel.getEndTime()));
        eventLocationET.setText(viewModel.getLocation());
    }

    private void initAlarmDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);
    }

    private void initFilesList() {
        viewModel.subscribeUriObserver();
        fileList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        uris = new ArrayList<>();
        Observer<ArrayList<Uri>> observerUris = urisNew -> {
            uris = new ArrayList<>(urisNew);
            AdapterCalendarFiles adapter = new AdapterCalendarFiles(uris, fileList);
            fileList.setAdapter(adapter);
        };
        viewModel.getUris().observe(this, observerUris);
        AdapterCalendarFiles adapter = new AdapterCalendarFiles(uris, fileList);
        fileList.setAdapter(adapter);
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

        endTimePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour,
                minute, true);
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        eventNameET = findViewById(R.id.txtEventTitle);
        eventLocationET = findViewById(R.id.eventLocationET);

        dateTV = findViewById(R.id.dateTV);
        startTimeTV = findViewById(R.id.startTimeTV);
        endTimeTV = findViewById(R.id.endTimeTV);

        selectDateBtn = findViewById(R.id.selectDateBtn);
        selectStartTimeBtn = findViewById(R.id.selectStartTimeBtn);
        selectEndTimeBtn = findViewById(R.id.selectEndTimeBtn);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);
        commentEventBtn = findViewById(R.id.commentEventBtn);
        addFilesBtn = findViewById(R.id.addFilesBtn);

        fileList = findViewById(R.id.fileList);
    }

    private void initListeners() {
        selectDateBtn.setOnClickListener(this::openDatePicker);
        selectStartTimeBtn.setOnClickListener(this::openStartTimePicker);
        selectEndTimeBtn.setOnClickListener(this::openEndTimePicker);
        saveChangesBtn.setOnClickListener(this::saveChangesAction);
        commentEventBtn.setOnClickListener(this::commentEventAction);
        addFilesBtn.setOnClickListener(this::addFilesAction);
    }

    private void addFilesAction(View view) {
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            DataBaseAdapter.uploadFile(uri, getApplicationContext(), viewModel.getEvent().getId());
        }
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

    private void saveChangesAction(View view) {
        String eventName = eventNameET.getText().toString();
        String location = eventLocationET.getText().toString();
        // TODO Implement IDs in events
        LocalTime startTime = viewModel.getStartTime();
        LocalTime endTime = viewModel.getEndTime();
        LocalDate date = CalendarUtils.selectedDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // TODO: El parse de les hores no estan be
        viewModel.editEvent(viewModel.getId(), eventName, location, false,
                date.format(formatter), startTime.toString(), endTime.toString(), group.getId());
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