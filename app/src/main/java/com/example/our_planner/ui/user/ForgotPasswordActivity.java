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

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;

public class ForgotPasswordActivity extends AppCompatActivity implements DataBaseAdapter.DBInterface {

    private EditText emailET;
    private Button receiveRecoveryEmailB;
    private Button goBackB;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert = builder.create();
        alert.setTitle(R.string.help);

        emailET = findViewById(R.id.emailET);
        receiveRecoveryEmailB = findViewById(R.id.receiveRecoveryEmailB);
        goBackB = findViewById(R.id.goBackB);
        receiveRecoveryEmailB.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            if (!email.equals("")) DataBaseAdapter.forgotPassword(this,email);
            else setToast("Fill the required field");
        });

        goBackB.setOnClickListener(view -> startActivity(new Intent(
                ForgotPasswordActivity.this, LoginActivity.class)));
    }

    @Override
    public void setToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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