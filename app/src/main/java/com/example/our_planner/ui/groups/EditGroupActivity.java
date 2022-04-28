package com.example.our_planner.ui.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
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
    private EditGroupActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        viewModel = new ViewModelProvider(this).get(EditGroupActivityViewModel.class);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);
        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());

        Group group = (Group) getIntent().getSerializableExtra("group");
        txtTitle.setText(group.getTitle());
        txtDetails.setText(group.getDetails());
        setColour(group.getColour());

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

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(view -> {
            String title = txtTitle.getText().toString();
            String details = txtDetails.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Title field is empty!", Toast.LENGTH_SHORT).show();
            } else if (title.equals(group.getTitle()) && details.equals(group.getDetails()) && currentColour == group.getColour()) {
                Toast.makeText(this, "No information has changed", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.editGroup(group.getId(), title, details, currentColour);
                finish();
            }
        });

        final Observer<String> observerToast = t -> Toast.makeText(EditGroupActivity.this, t, Toast.LENGTH_SHORT).show();
        viewModel.getToast().observe(this, observerToast);
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
                        setColour(colour);
                    }
                });
        colourPicker.show();
    }

    private void setColour(int colour) {
        currentColour = colour;
        colourView.setBackgroundColor(colour);
    }
}
