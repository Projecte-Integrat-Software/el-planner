package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;
import com.example.our_planner.ThemeSwitcher;
import com.example.our_planner.model.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditGroupActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtDetails;
    private View colourView;
    private int currentColour;
    private RecyclerView recyclerViewParticipants;
    private EditGroupActivityViewModel viewModel;
    private AlertDialog alert;
    private boolean admin;
    private TextView txtUser, groupTitle, groupDetails, groupColour, groupParticipants;
    private Button saveChangesBtn;
    private FloatingActionButton btnParticipant;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        viewModel = new ViewModelProvider(this).get(EditGroupActivityViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = findViewById(R.id.txtGroupTitle);
        txtDetails = findViewById(R.id.txtGroupDetails);
        colourView = findViewById(R.id.selected_colour);
        colourView.setOnClickListener(view -> chooseColour());
        groupTitle = findViewById(R.id.labelGroupTitle);
        groupDetails = findViewById(R.id.labelGroupDetails);
        groupColour = findViewById(R.id.labelGroupColour);
        groupParticipants = findViewById(R.id.labelGroupParticipants);
        txtUser = findViewById(R.id.txtUser);

        group = (Group) getIntent().getSerializableExtra("group");
        txtTitle.setText(group.getTitle());
        txtDetails.setText(group.getDetails());
        setColour(viewModel.getColour(group));
        admin = group.getAdmins().get(viewModel.getEmail());
        CheckBox cb = findViewById(R.id.cbUserAdmin);
        cb.setChecked(admin);

        recyclerViewParticipants = findViewById(R.id.recyclerViewParticipants);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParticipants.setAdapter(new AdapterParticipants(this, group.getParticipants(), group.getAdmins()));

        btnParticipant = findViewById(R.id.btnParticipant);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);

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

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
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
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(EditGroupActivity.this, NavigationDrawer.class);
            intent.putExtra("fragmentToLoad", "Groups");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {
        Resources r = LocaleLanguage.getLocale(this).getResources();

        setTitle(r.getString(R.string.edit_group));
        txtUser.setText(viewModel.getUserName() + " (" + r.getString(R.string.you) + ")");
        txtTitle.setHint(r.getString(R.string.group_title));
        txtDetails.setHint(r.getString(R.string.group_details));
        groupTitle.setText(r.getString(R.string.title));
        groupDetails.setText(r.getString(R.string.details));
        groupColour.setText(r.getString(R.string.colour));
        groupParticipants.setText(r.getString(R.string.participants));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(admin ? r.getString(R.string.help_edit_group_admin) : r.getString(R.string.help_edit_group));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));

        if (admin) {
            btnParticipant.setOnClickListener(view -> {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_invite_participants, null, false), 900, 1000, true);
                pw.setBackgroundDrawable(getDrawable(ThemeSwitcher.lightThemeSelected() ? R.drawable.rounded_corners : R.drawable.rounded_corners_dark));
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);

                ((TextView) pw.getContentView().findViewById(R.id.txtInviteParticipants)).setText(r.getString(R.string.invite_participants));
                ((TextView) pw.getContentView().findViewById(R.id.txtParticipantsInvite)).setText(r.getString(R.string.participants_to_invite));

                EditText emailTxt = pw.getContentView().findViewById(R.id.txtParticipantEmail);
                emailTxt.setHint(r.getString(R.string.email));
                RecyclerView recyclerView = pw.getContentView().findViewById(R.id.recycleViewParticipantsToInvite);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new AdapterParticipantsInvite(viewModel.getInvitationEmails()));

                Button addBtn = pw.getContentView().findViewById(R.id.addEmailBtn);
                addBtn.setText(r.getString(R.string.add));
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailTxt.getText().toString();
                        final Observer<Boolean> observerRegistered = b -> addEmail(email, b);
                        viewModel.resetRegistered();
                        viewModel.getRegistered().observe(EditGroupActivity.this, observerRegistered);
                        if (email.isEmpty()) {
                            Toast.makeText(EditGroupActivity.this, r.getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
                        } else if (email.equals(viewModel.getEmail())) {
                            Toast.makeText(EditGroupActivity.this, r.getString(R.string.invite_yourself), Toast.LENGTH_SHORT).show();
                        } else {
                            viewModel.checkRegistered(email);
                        }
                    }

                    private void addEmail(String email, boolean b) {
                        if (b) {
                            if (group.getParticipants().containsKey(email)) {
                                Toast.makeText(EditGroupActivity.this, r.getString(R.string.user_already_group), Toast.LENGTH_SHORT).show();
                            } else if (!((AdapterParticipantsInvite) recyclerView.getAdapter()).addElement(email)) {
                                Toast.makeText(EditGroupActivity.this, r.getString(R.string.user_invited), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditGroupActivity.this, r.getString(R.string.no_user_registered), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button btnCancel = pw.getContentView().findViewById(R.id.cancelBtn);
                btnCancel.setOnClickListener(view1 -> pw.dismiss());
                btnCancel.setText(r.getString(R.string.cancel));

                Button btnInvite = pw.getContentView().findViewById(R.id.saveInvitationsBtn);
                btnInvite.setText(r.getString(R.string.save));
                btnInvite.setOnClickListener(view1 -> {
                    viewModel.saveInvitationEmails(((AdapterParticipantsInvite) recyclerView.getAdapter()).getParticipantEmails());
                    pw.dismiss();
                });
            });
        } else {
            txtTitle.setFocusable(false);
            txtDetails.setFocusable(false);
            btnParticipant.setVisibility(View.INVISIBLE);
        }

        saveChangesBtn.setText(r.getString(R.string.save_changes));
        saveChangesBtn.setOnClickListener(view -> {
            String title = txtTitle.getText().toString();
            String details = txtDetails.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, r.getString(R.string.title_empty), Toast.LENGTH_SHORT).show();
            } else {
                viewModel.editGroup(group.getId(), title, details, currentColour, group.getParticipants(), group.getAdmins(), group.getEvents());
                finish();
            }
        });
    }
}
