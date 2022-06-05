package com.example.our_planner.ui.user;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.LocaleLanguage;
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
    private TextView txtCreateAccount, txtFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtCreateAccount = findViewById(R.id.txtCreate);
        txtFields = findViewById(R.id.txtRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
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

        setTitle(r.getString(R.string.title_activity_register));
        txtCreateAccount.setText(r.getString(R.string.create_account));
        txtFields.setText(r.getString(R.string.fill_fields));
        txtUsername.setHint(r.getString(R.string.username));
        txtEmail.setHint(r.getString(R.string.email));
        txtPassword.setHint(r.getString(R.string.password));
        txtRepeatPassword.setHint(r.getString(R.string.confirm_password));
        btnRegister.setText(r.getString(R.string.register));
        btnCancel.setText(r.getString(R.string.cancel));

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.help_register));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));

        btnRegister.setOnClickListener(view -> {
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String email = txtEmail.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, r.getString(R.string.username_empty), Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(RegisterActivity.this, r.getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, r.getString(R.string.password_empty), Toast.LENGTH_SHORT).show();
            } else if (txtRepeatPassword.getText().toString().equals(password)) {
                viewModel.register(email, password, username, getResources().getDrawable(R.drawable.ic_launcher_foreground));
            } else {
                Toast.makeText(RegisterActivity.this, r.getString(R.string.passwords_different), Toast.LENGTH_SHORT).show();
            }
        });

        final Observer<String> observerToast = t -> {
            if (t.equals("-")) {
                Toast.makeText(RegisterActivity.this, r.getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, NavigationDrawer.class));
            } else {
                Toast.makeText(RegisterActivity.this, t, Toast.LENGTH_SHORT).show();
            }
        };

        viewModel.getToast().removeObservers(this);
        viewModel.getToast().observe(this, observerToast);
    }
}