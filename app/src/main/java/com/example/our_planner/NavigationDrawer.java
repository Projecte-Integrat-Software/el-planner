package com.example.our_planner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.our_planner.ui.calendar.CalendarFragment;
import com.example.our_planner.ui.groups.GroupsFragment;
import com.example.our_planner.ui.invitations.InvitationsFragment;
import com.example.our_planner.ui.settings.SettingsFragment;
import com.example.our_planner.ui.user.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private NavController navController;
    private ImageView profilePictureND;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navCalendar, R.id.navGroups, R.id.navInvitations, R.id.navSettings, R.id.navLogOut)
                .setOpenableLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        //Start specific fragment if wanted
        String s = getIntent().getStringExtra("fragment");
        if (s != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (s) {
                case "Calendar": {
                    navigationView.getMenu().getItem(0).setChecked(true);
                    fragmentTransaction.replace(R.id.fragment_container, new CalendarFragment()).commit();
                    break;
                }
                case "Groups": {
                    navigationView.getMenu().getItem(1).setChecked(true);
                    fragmentTransaction.replace(R.id.fragment_container, new GroupsFragment()).commit();
                    break;
                }
                case "Invitations": {
                    navigationView.getMenu().getItem(2).setChecked(true);
                    fragmentTransaction.replace(R.id.fragment_container, new InvitationsFragment()).commit();
                    break;
                }
                case "Settings": {
                    navigationView.getMenu().getItem(3).setChecked(true);
                    fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment()).commit();
                    break;
                }
            }
            getSupportActionBar().setTitle(s);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onSupportNavigateUp() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profilePictureND = navigationView.findViewById(R.id.profilePictureND);
        TextView username = navigationView.findViewById(R.id.username);
        TextView email = navigationView.findViewById(R.id.email);

        updateProfilePicture();
        username.setText(DataBaseAdapter.getUserName());
        email.setText(DataBaseAdapter.getEmail());

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.navCalendar:{
                fragmentTransaction.replace(R.id.fragment_container, new CalendarFragment()).commit();
                break;
            }
            case R.id.navGroups:{
                fragmentTransaction.replace(R.id.fragment_container, new GroupsFragment()).commit();
                break;
            }
            case R.id.navInvitations: {
                fragmentTransaction.replace(R.id.fragment_container, new InvitationsFragment()).commit();
                break;
            }
            case R.id.navSettings: {
                fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            }
            case R.id.navLogOut: {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_log_out, null, false), 900, 400, true);
                pw.showAtLocation(this.findViewById(R.id.fragment_container), Gravity.CENTER, 0, 0);

                Button cancelBtn = pw.getContentView().findViewById(R.id.cancelBtn);
                cancelBtn.setOnClickListener(view -> pw.dismiss());

                Button yesBtn = pw.getContentView().findViewById(R.id.yesBtn);
                yesBtn.setOnClickListener(view -> {
                    DataBaseAdapter.logOut();
                    pw.dismiss();
                    startActivity(new Intent(NavigationDrawer.this, LoginActivity.class));
                });

                return true;
            }
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateProfilePicture() {
        Task<byte[]> task = DataBaseAdapter.updateProfilePicture(getResources().getDrawable(R.drawable.ic_launcher_foreground));
        task.addOnCompleteListener(task1 -> {
            byte[] byteArray = DataBaseAdapter.getByteArray();
            profilePictureND.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.length));
        });
    }
}