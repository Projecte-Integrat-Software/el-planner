package com.example.our_planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements DataBaseAdapter.InterfaceDB {

    private Button btnCancel;
    private Button btnRegister;
    private EditText txtRepeatPassword;
    private EditText txtPassword;
    private EditText txtEmail;
    private EditText txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                DataBaseAdapter.register(RegisterActivity.this, email, password, username);
            } else {
                Toast.makeText(RegisterActivity.this, "Passwords are not the same!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, NavigationDrawer.class));
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}