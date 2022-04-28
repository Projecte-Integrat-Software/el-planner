package com.example.our_planner.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;

public class ForgotPasswordActivity extends AppCompatActivity implements DataBaseAdapter.DBInterface {

    private EditText emailET;
    private Button receiveRecoveryEmailB;
    private Button goBackB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET = findViewById(R.id.emailET);
        receiveRecoveryEmailB = findViewById(R.id.receiveRecoveryEmailB);
        goBackB = findViewById(R.id.goBackB);

        //TODO: Connect with ViewModel to check if the username exists, and it it does send the
        // recovery email.
        receiveRecoveryEmailB.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            if (!email.equals("")) DataBaseAdapter.forgotPassword(this,email);
        });

        goBackB.setOnClickListener(view -> startActivity(new Intent(
                ForgotPasswordActivity.this, LoginActivity.class)));
    }

    @Override
    public void setToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}