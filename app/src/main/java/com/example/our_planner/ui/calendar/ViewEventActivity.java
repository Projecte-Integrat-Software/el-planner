package com.example.our_planner.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;

public class ViewEventActivity extends AppCompatActivity {

    private Button editEventBtn;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        event = (Event) getIntent().getSerializableExtra("event");

        initWidgets();
        initListeners();
    }

    private void initWidgets() {
        editEventBtn = findViewById(R.id.editEventBtn);
    }

    private void initListeners() {
        editEventBtn.setOnClickListener(this::openEditEventActivity);
    }

    private void openEditEventActivity(View view) {
        Intent i = new Intent(this, EditEventActivity.class);
        i.putExtra("event", event);
        this.startActivity(i);
    }


}