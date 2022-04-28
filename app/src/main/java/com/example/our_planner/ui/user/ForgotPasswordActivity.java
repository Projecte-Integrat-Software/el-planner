package com.example.our_planner.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.our_planner.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText usernameET;
    private Button receiveRecoveryEmailB;
    private Button goBackB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameET = findViewById(R.id.usernameET);
        receiveRecoveryEmailB = findViewById(R.id.receiveRecoveryEmailB);
        goBackB = findViewById(R.id.goBackB);

        //TODO: Connect with ViewModel to check if the username exists, and it it does send the
        // recovery email.
        receiveRecoveryEmailB.setOnClickListener(view -> {
            String username = usernameET.getText().toString();
            Toast.makeText(getApplicationContext(), "Sending recovery email to: " + username,
                    Toast.LENGTH_LONG).show();
        });

        goBackB.setOnClickListener(view -> startActivity(new Intent(
                ForgotPasswordActivity.this, LoginActivity.class)));
    }
}