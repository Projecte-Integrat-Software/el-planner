package com.example.our_planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements DataBaseAdapter.InterfaceDB {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSign;
    private Button btnPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //If user already logged in, we omit the activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
        }

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSign = findViewById(R.id.btnSign);
        btnSign.setOnClickListener(view -> {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
            } else {
                DataBaseAdapter.login(LoginActivity.this, email, password);
            }
        });

        btnPassword = findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}