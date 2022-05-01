package com.example.our_planner.ui.groups;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtDetails;
    private View colourView;
    private int currentColour;
    private RecyclerView recyclerViewParticipants;
    private CreateGroupActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        viewModel = new ViewModelProvider(this).get(CreateGroupActivityViewModel.class);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);
        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());
        TextView user = findViewById(R.id.txtUser);
        user.setText(viewModel.getUserName() + " (You)");

        //Default colour: black
        currentColour = Color.BLACK;

        recyclerViewParticipants = findViewById(R.id.recyclerViewParticipants);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParticipants.setAdapter(new AdapterParticipants(new HashMap<>(), new HashMap<>()));

        FloatingActionButton btnParticipant = findViewById(R.id.btnParticipant);
        btnParticipant.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_invite_participants, null, false), 900, 1000, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

            EditText emailTxt = pw.getContentView().findViewById(R.id.txtParticipantEmail);
            RecyclerView recyclerView = pw.getContentView().findViewById(R.id.recycleViewParticipantsToInvite);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new AdapterParticipantsInvite(viewModel.getInvitationEmails()));
            pw.getContentView().findViewById(R.id.addEmailBtn).setOnClickListener(view1 -> {
                String email = emailTxt.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(CreateGroupActivity.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Check in database if email exists!!
                    if (!((AdapterParticipantsInvite) recyclerView.getAdapter()).addElement(email)) {
                        Toast.makeText(CreateGroupActivity.this, "User already selected to be invited!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            pw.getContentView().findViewById(R.id.cancelBtn).setOnClickListener(view1 -> pw.dismiss());
            pw.getContentView().findViewById(R.id.saveInvitationsBtn).setOnClickListener(view1 -> {
                viewModel.saveInvitationEmails(((AdapterParticipantsInvite) recyclerView.getAdapter()).getParticipantEmails());
                pw.dismiss();
            });
        });

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(view -> {
            //TODO: Invite participants!
            String title = txtTitle.getText().toString();
            String details = txtDetails.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Title field is empty!", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.createGroup(title, details, currentColour);
                finish();
            }
        });

        final Observer<String> observerToast = t -> Toast.makeText(CreateGroupActivity.this, t, Toast.LENGTH_SHORT).show();
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
                        currentColour = colour;
                        colourView.setBackgroundColor(currentColour);
                    }
                });
        colourPicker.show();
    }
}
