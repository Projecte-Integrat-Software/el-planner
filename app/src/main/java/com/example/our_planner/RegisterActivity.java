package com.example.our_planner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class  RegisterActivity extends AppCompatActivity{

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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String email = txtEmail.getText().toString();
                Toast.makeText(getApplicationContext(), "Username: " + username + "\nEmail" + email + "\nPassword" + password + "\n", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegisterActivity.this, NavigationDrawer.class));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}