package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.example.our_planner.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditGroupActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtDetails;
    private View colourView;
    private int currentColour;
    private RecyclerView recyclerViewParticipants;
    private EditGroupActivityViewModel viewModel;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        viewModel = new ViewModelProvider(this).get(EditGroupActivityViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);
        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());

        Group group = (Group) getIntent().getSerializableExtra("group");
        txtTitle.setText(group.getTitle());
        txtDetails.setText(group.getDetails());
        setColour(viewModel.getColour(group));
        TextView user = findViewById(R.id.txtUser);
        user.setText(viewModel.getUserName() + " (You)");
        Map<String, User> participants = group.getParticipants();
        Map<String, Boolean> admins = group.getAdmins();
        boolean b = admins.get(viewModel.getEmail());
        CheckBox cb = findViewById(R.id.cbUserAdmin);
        cb.setChecked(b);

        recyclerViewParticipants = findViewById(R.id.recyclerViewParticipants);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParticipants.setAdapter(new AdapterParticipants(this, participants, admins));

        //Allow inviting participants only if admin
        FloatingActionButton btnParticipant = findViewById(R.id.btnParticipant);
        if (b) {
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
                        viewModel.getRegistered().observe(EditGroupActivity.this, observerRegistered);
                        if (email.isEmpty()) {
                            Toast.makeText(EditGroupActivity.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
                        } else if (email.equals(viewModel.getEmail())) {
                            Toast.makeText(EditGroupActivity.this, "You cannot invite yourself!", Toast.LENGTH_SHORT).show();
                        } else {
                            viewModel.checkRegistered(email);
                        }
                    }

                    private void addEmail(String email, boolean b) {
                        if (b) {
                            if (participants.containsKey(email)) {
                                Toast.makeText(EditGroupActivity.this, "This user is already in the group!", Toast.LENGTH_SHORT).show();
                            } else if (!((AdapterParticipantsInvite) recyclerView.getAdapter()).addElement(email)) {
                                Toast.makeText(EditGroupActivity.this, "User already selected to be invited!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditGroupActivity.this, "There is no user registered with this email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                pw.getContentView().findViewById(R.id.cancelBtn).setOnClickListener(view1 -> pw.dismiss());
                pw.getContentView().findViewById(R.id.saveInvitationsBtn).setOnClickListener(view1 -> {
                    viewModel.saveInvitationEmails(((AdapterParticipantsInvite) recyclerView.getAdapter()).getParticipantEmails());
                    pw.dismiss();
                });
            });
        } else {
            txtTitle.setFocusable(false);
            txtDetails.setFocusable(false);
            btnParticipant.setVisibility(View.INVISIBLE);
        }

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(view -> {
            String title = txtTitle.getText().toString();
            String details = txtDetails.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Title field is empty!", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println(participants);
                System.out.println(admins);
                viewModel.editGroup(group.getId(), title, details, currentColour, participants, admins, group.getEvents());
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
