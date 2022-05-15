package com.example.our_planner.ui.user;

import android.content.DialogInterface;
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

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSign;
    private Button btnPassword;
    private Button btnRegister;
    private LoginActivityViewModel viewModel;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        //If user already logged in, we omit the activity
        if (viewModel.isUserLogged()) {
            startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
        }

        setTitle(R.string.title_activity_sign_in);

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.help_login);
        builder.setNeutralButton(R.string.close, (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(R.string.help);

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
                viewModel.login(email, password);
            }
        });

        btnPassword = findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        final Observer<String> observerToast = t -> {
            Toast.makeText(LoginActivity.this, t, Toast.LENGTH_SHORT).show();
            if (t.startsWith("Logged as")) {
                startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
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