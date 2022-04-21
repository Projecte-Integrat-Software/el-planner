package com.example.our_planner.ui.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditGroupActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtDetails;
    private View colourView;
    private int currentColour;
    private RecyclerView recyclerViewParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);

        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());

        //TODO: Get the group from the database and complete the title, details, colour and participants from it

        recyclerViewParticipants = findViewById(R.id.recyclerViewParticipants);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(this));
        //Dummy list to run the test of the recycler view without the database
        ArrayList<User> participants = new ArrayList<>();
        participants.add(new User("Diego"));
        AdapterParticipants adapterParticipants = new AdapterParticipants(participants);
        recyclerViewParticipants.setAdapter(adapterParticipants);

        FloatingActionButton btnParticipant = findViewById(R.id.btnParticipant);
        btnParticipant.setOnClickListener(view -> {
            //TODO: Add a dialogue in which you can send invitations to the group to several participants
            Toast.makeText(getApplicationContext(), "Inviting participants", Toast.LENGTH_SHORT).show();
        });

        //TODO: Check everything and edit the group
        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(view -> {
            String title = txtTitle.getText().toString();
            String details = txtDetails.getText().toString();
            Toast.makeText(getApplicationContext(), "Group edited!\nTitle: " + title + "\nDetails: " + details + "\nColour: " + currentColour, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    public void chooseColour() {
        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        final AmbilWarnaDialog colourPicker = new AmbilWarnaDialog(this, currentColour,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        //Closes without doing anything
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int colour) {
                        //We change the colour selected
                        currentColour = colour;
                        colourView.setBackgroundColor(currentColour);
                    }
                });
        colourPicker.show();
    }
}
