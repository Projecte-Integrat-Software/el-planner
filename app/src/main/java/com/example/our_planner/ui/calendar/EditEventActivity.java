package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.our_planner.LocaleLanguage;
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
    private Button selectDateBtn, selectStartTimeBtn, selectEndTimeBtn, commentEventBtn,
            addFilesBtn, deleteEventBtn;

    private Group group;
    private ArrayList<Group> groups;
    private boolean admin;
    private RecyclerView fileList;

    private int position;

    private androidx.appcompat.app.AlertDialog alert;
    private ArrayList<Uri> uris;

    AdapterCalendarFiles filesAdapter;

    private boolean editing = false;

    private Menu menu;

    public boolean isEditing() {
        return editing;
    }

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
        viewModel.setGroup((Group) getIntent().getSerializableExtra("group"));

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

            position = groups.indexOf(viewModel.getGroup());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectGroup.setAdapter(adapter);
            selectGroup.setSelection(position);
        };
        viewModel.getAdminGroups().observe(this, observerGroups);

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

        //Esbrinem si som admin del grup i configurem l'activitat adientment
        admin = viewModel.getGroup().getAdmins().get(DataBaseAdapter.getEmail());
        configureVisibility();

        setTitle(LocaleLanguage.getLocale(this).getResources().getString(R.string.title_activity_view_event));
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
        Resources r = LocaleLanguage.getLocale(this).getResources();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.help_create_event));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));
    }

    private void initFilesList() {
        viewModel.subscribeUriObserver();
        fileList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        uris = new ArrayList<>();
        viewModel.getUris().observe(this, urisNew -> {
            uris = new ArrayList<>(urisNew);
            filesAdapter = new AdapterCalendarFiles(uris, fileList, this);
            fileList.swapAdapter(filesAdapter, true);
        });
        filesAdapter = new AdapterCalendarFiles(uris, fileList, this);
        fileList.setAdapter(filesAdapter);
    }

    //TODO the picker begins one month ahead
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month++;
            viewModel.setDate(LocalDate.of(year, month, day));
            dateTV.setText(CalendarUtils.formattedDate(viewModel.getDate()));
        };

        int year = viewModel.getDate().getYear();
        int month = viewModel.getDate().getMonthValue();
        int day = viewModel.getDate().getDayOfMonth();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month - 1, day);
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
        commentEventBtn = findViewById(R.id.commentEventBtn);
        addFilesBtn = findViewById(R.id.addFilesBtn);
        deleteEventBtn = findViewById(R.id.deleteEventBtn);

        fileList = findViewById(R.id.fileList);
    }

    private void initListeners() {
        selectDateBtn.setOnClickListener(this::openDatePicker);
        selectStartTimeBtn.setOnClickListener(this::openStartTimePicker);
        selectEndTimeBtn.setOnClickListener(this::openEndTimePicker);
        commentEventBtn.setOnClickListener(this::commentEventAction);
        addFilesBtn.setOnClickListener(this::addFilesAction);
        deleteEventBtn.setOnClickListener(this::deleteEventAction);
    }

    private void addFilesAction(View view) {
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Resources r = LocaleLanguage.getLocale(this).getResources();
        try {
            startActivityForResult(Intent.createChooser(intent, r.getString(R.string.select_file)), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, r.getString(R.string.install_file_manager), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            viewModel.addNewUri(uri);
            filesAdapter = new AdapterCalendarFiles(viewModel.getNewUris(), fileList, this);
            fileList.swapAdapter(filesAdapter, true);
            filesAdapter.setVisibilityRemoveButton(View.VISIBLE);
        }
    }

    public EditEventActivityViewModel getViewModel() {
        return viewModel;
    }

    public void swapAdapter() {
        filesAdapter = new AdapterCalendarFiles(viewModel.getNewUris(), fileList, this);
        fileList.swapAdapter(filesAdapter, true);
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

    private void saveChangesAction() {
        String eventName = eventNameET.getText().toString();
        String location = eventLocationET.getText().toString();
        LocalTime startTime = viewModel.getStartTime();
        LocalTime endTime = viewModel.getEndTime();
        LocalDate date = viewModel.getDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        viewModel.editEvent(viewModel.getId(), eventName, location,
                date.format(formatter), startTime.toString(), endTime.toString(), group.getId());
    }

    private void deleteEventAction(View view) {
        viewModel.deleteEvent();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        if (admin) {
            getMenuInflater().inflate(R.menu.edit_event_toolbar, menu);
        } else {
            getMenuInflater().inflate(R.menu.toolbar, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Resources r = LocaleLanguage.getLocale(this).getResources();
        int id = item.getItemId();
        if (id == R.id.help) {
            alert.setMessage(r.getString(getTitle().equals(r.getString(R.string.title_activity_view_event)) ? R.string.help_view_event : R.string.help_edit_event));
            alert.show();
        } else if (id == R.id.edit) {
            setTitle(r.getString(R.string.edit_event));

            menu.clear();
            getMenuInflater().inflate(R.menu.save_edit_event_toolbar, menu);

            eventNameET.setEnabled(true);
            eventLocationET.setEnabled(true);
            selectGroup.setEnabled(true);
            selectDateBtn.setVisibility(View.VISIBLE);
            selectStartTimeBtn.setVisibility(View.VISIBLE);
            selectEndTimeBtn.setVisibility(View.VISIBLE);
            addFilesBtn.setVisibility(View.VISIBLE);
            filesAdapter.setVisibilityRemoveButton(View.VISIBLE);

            editing = true;
            filesAdapter.updateParent(this);

            viewModel.setNewUris(viewModel.getUris().getValue());
        } else if (id == R.id.save_changes) {
            // Load files
            for (Uri uri : viewModel.getNewUris()) {
                if (!viewModel.getUris().getValue().contains(uri)) {
                    DataBaseAdapter.uploadFile(uri, getApplicationContext(), viewModel.getEvent().getId());
                }
            }
            for (Uri uri : viewModel.getUris().getValue()) {
                if (!viewModel.getNewUris().contains(uri)) {
                    DataBaseAdapter.removeFile(uri, getApplicationContext(), viewModel.getEvent().getId());
                }
            }
            viewModel.updateUris(viewModel.getNewUris());
            filesAdapter = new AdapterCalendarFiles(viewModel.getUris().getValue(), fileList, this);
            fileList.swapAdapter(filesAdapter, true);

            setTitle(r.getString(R.string.title_activity_view_event));
            menu.clear();
            getMenuInflater().inflate(R.menu.edit_event_toolbar, menu);

            eventNameET.setEnabled(false);
            eventLocationET.setEnabled(false);
            selectGroup.setEnabled(false);
            selectDateBtn.setVisibility(View.INVISIBLE);
            selectStartTimeBtn.setVisibility(View.INVISIBLE);
            selectEndTimeBtn.setVisibility(View.INVISIBLE);
            addFilesBtn.setVisibility(View.INVISIBLE);
            filesAdapter.setVisibilityRemoveButton(View.INVISIBLE);


            editing = false;
            filesAdapter.updateParent(this);

            //Guardem els canvis
            saveChangesAction();
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureVisibility() {
        eventNameET.setEnabled(false);
        eventLocationET.setEnabled(false);
        selectGroup.setEnabled(false);
        selectDateBtn.setVisibility(View.INVISIBLE);
        selectStartTimeBtn.setVisibility(View.INVISIBLE);
        selectEndTimeBtn.setVisibility(View.INVISIBLE);
        addFilesBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
    }

    private void changeLanguage() {
        Resources r = LocaleLanguage.getLocale(this).getResources();

        ((TextView) findViewById(R.id.labelEventTitle)).setText(r.getString(R.string.title));
        eventNameET.setHint(r.getString(R.string.event_name));
        ((TextView) findViewById(R.id.labelEventLocation)).setText(r.getString(R.string.location));
        ((TextView) findViewById(R.id.labelSelectGroup)).setText(r.getString(R.string.select_group));
        eventLocationET.setHint(r.getString(R.string.location));
        selectDateBtn.setText(r.getString(R.string.select_date));
        selectStartTimeBtn.setText(r.getString(R.string.select_start_time));
        selectEndTimeBtn.setText(r.getString(R.string.select_end_time));
        commentEventBtn.setText(r.getString(R.string.comment_event));
        addFilesBtn.setText(r.getString(R.string.add_files));
        deleteEventBtn.setText(r.getString(R.string.delete_event));
    }
}