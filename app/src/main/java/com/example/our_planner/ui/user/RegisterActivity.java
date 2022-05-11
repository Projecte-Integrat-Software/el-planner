package com.example.our_planner.ui.user;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnRegister;
    private EditText txtRepeatPassword;
    private EditText txtPassword;
    private EditText txtEmail;
    private EditText txtUsername;
    private RegisterActivityViewModel viewModel;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.help_register);
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);

        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(view -> {
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String email = txtEmail.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Username field is empty!", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
            } else if (txtRepeatPassword.getText().toString().equals(password)) {
                viewModel.register(email, password, username, getResources().getDrawable(R.drawable.ic_launcher_foreground));
            } else {
                Toast.makeText(RegisterActivity.this, "Passwords are not the same!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        final Observer<String> observerToast = t -> {
            Toast.makeText(RegisterActivity.this, t, Toast.LENGTH_SHORT).show();
            if (t.equals("Registered successfully")) {
                startActivity(new Intent(RegisterActivity.this, NavigationDrawer.class));
            }
        };

        viewModel.getToast().observe(this, observerToast);
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