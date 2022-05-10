package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtDetails;
    private View colourView;
    private int currentColour;
    private CreateGroupActivityViewModel viewModel;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        viewModel = new ViewModelProvider(this).get(CreateGroupActivityViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);
        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());
        TextView user = findViewById(R.id.txtUser);
        user.setText(viewModel.getUserName() + " (You)");

        //Default colour: black
        currentColour = Color.BLACK;

        FloatingActionButton btnParticipant = findViewById(R.id.btnParticipant);
        btnParticipant.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_invite_participants, null, false), 900, 1000, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

            EditText emailTxt = pw.getContentView().findViewById(R.id.txtParticipantEmail);
            RecyclerView recyclerView = pw.getContentView().findViewById(R.id.recycleViewParticipantsToInvite);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new AdapterParticipantsInvite(viewModel.getInvitationEmails()));
            pw.getContentView().findViewById(R.id.addEmailBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailTxt.getText().toString();
                    final Observer<Boolean> observerRegistered = b -> addEmail(email, b);
                    viewModel.resetRegistered();
                    viewModel.getRegistered().observe(CreateGroupActivity.this, observerRegistered);
                    if (email.isEmpty()) {
                        Toast.makeText(CreateGroupActivity.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
                    } else if (email.equals(viewModel.getEmail())) {
                        Toast.makeText(CreateGroupActivity.this, "You cannot invite yourself!", Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.checkRegistered(email);
                    }
                }

                private void addEmail(String email, boolean b) {
                    if (b) {
                        if (!((AdapterParticipantsInvite) recyclerView.getAdapter()).addElement(email)) {
                            Toast.makeText(CreateGroupActivity.this, "User already selected to be invited!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "There is no user registered with this email", Toast.LENGTH_SHORT).show();
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
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(CreateGroupActivity.this, NavigationDrawer.class);
            intent.putExtra("fragmentToLoad", "Groups");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
