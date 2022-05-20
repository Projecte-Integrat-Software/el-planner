package com.example.our_planner.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;

public class ForgotPasswordActivity extends AppCompatActivity implements DataBaseAdapter.DBInterface {

    private EditText emailET;
    private Button receiveRecoveryEmailB;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailET = findViewById(R.id.emailET);
        receiveRecoveryEmailB = findViewById(R.id.receiveRecoveryEmailB);
    }

    @Override
    public void setToast(String s) {
        if (s.equals("-")) {
            Toast.makeText(this, LocaleLanguage.getLocale(this).getResources().getString(R.string.instructions_sent), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
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

    private void changeLanguage() {
        Resources r = LocaleLanguage.getLocale(this).getResources();

        setTitle(r.getString(R.string.title_activity_forgot_password));
        emailET.setHint(r.getString(R.string.email));
        receiveRecoveryEmailB.setText(r.getString(R.string.receive_recovery_email));
        receiveRecoveryEmailB.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            if (!email.equals("")) DataBaseAdapter.forgotPassword(this,email);
            else setToast(r.getString(R.string.email_empty));
        });

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.help_forgot_password));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));
    }
}