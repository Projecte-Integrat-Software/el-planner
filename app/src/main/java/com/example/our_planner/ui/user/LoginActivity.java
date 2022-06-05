package com.example.our_planner.ui.user;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.our_planner.ThemeSwitcher;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSign;
    private Button btnPassword;
    private Button btnRegister;
    private LoginActivityViewModel viewModel;
    private AlertDialog alert;
    private TextView txtWelcome, txtSignInRegister;
    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSwitcher.updateTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        //If user already logged in, we omit the activity
        if (viewModel.isUserLogged()) {
            goToCalendar();
        }

        txtWelcome = findViewById(R.id.txtWelcome);
        txtSignInRegister = findViewById(R.id.txtSignInRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSign = findViewById(R.id.btnSign);
        btnPassword = findViewById(R.id.btnPassword);
        btnRegister = findViewById(R.id.btnRegister);
        imgLogo = findViewById(R.id.imgLogo);
    }

    @Override
    public void onResume() {
        super.onResume();
        imgLogo.setImageDrawable(getDrawable(ThemeSwitcher.lightThemeSelected() ? R.drawable.logo_ourplanner : R.drawable.logo_ourplanner_white_version));
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

        setTitle(r.getString(R.string.title_activity_sign_in));
        txtWelcome.setText(r.getString(R.string.welcome));
        txtSignInRegister.setText(r.getString(R.string.sign_in_register));
        txtEmail.setHint(r.getString(R.string.email));
        txtPassword.setHint(r.getString(R.string.password));
        btnSign.setText(r.getString(R.string.sign_in));
        btnPassword.setText(r.getString(R.string.forgot_password));
        btnRegister.setText(r.getString(R.string.register));

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.help_login));
        builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
        alert = builder.create();
        alert.setTitle(r.getString(R.string.help));

        btnSign.setOnClickListener(view -> {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, r.getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, r.getString(R.string.password_empty), Toast.LENGTH_SHORT).show();
            } else {
                viewModel.login(email, password);
            }
        });

        btnPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        final Observer<String> observerToast = t -> {
            if (t.startsWith(" ")) {
                Toast.makeText(LoginActivity.this, r.getString(R.string.logged_as) + t, Toast.LENGTH_SHORT).show();
                goToCalendar();
            } else {
                Toast.makeText(LoginActivity.this, t, Toast.LENGTH_SHORT).show();
            }
        };

        viewModel.getToast().removeObservers(this);
        viewModel.getToast().observe(this, observerToast);
    }

    private void goToCalendar() {
        Intent i = new Intent(LoginActivity.this, NavigationDrawer.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}