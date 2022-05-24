package com.example.our_planner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private String choice;
    private FragmentManager fragmentManager;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navCalendar, R.id.navGroups, R.id.navInvitations, R.id.navSettings, R.id.navLogOut)
                .setOpenableLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        username.setText(DataBaseAdapter.getUserName());
        TextView email = headerView.findViewById(R.id.email);
        email.setText(DataBaseAdapter.getEmail());
        profilePictureND = headerView.findViewById(R.id.profilePictureND);
        updateProfilePicture();

        choice = "Calendar";
        //Start specific fragment if wanted
        String s = getIntent().getStringExtra("fragmentToLoad");
        if (s != null) {
            switch (s) {
                case "Calendar": {
                    navigationView.getMenu().getItem(0).setChecked(true);
                    changeFragment(new CalendarFragment(), true);
                    break;
                }
                case "Groups": {
                    navigationView.getMenu().getItem(1).setChecked(true);
                    changeFragment(new GroupsFragment(), true);
                    break;
                }
                case "Invitations": {
                    navigationView.getMenu().getItem(2).setChecked(true);
                    changeFragment(new InvitationsFragment(), true);
                    break;
                }
                case "Settings": {
                    navigationView.getMenu().getItem(3).setChecked(true);
                    changeFragment(new SettingsFragment(), true);
                    break;
                }
            }
            choice = s;
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
        DataBaseAdapter.checkInvitations(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Context c = LocaleLanguage.getLocale(this);
        Resources r = c.getResources();
        String helpMessage = "";

        switch(choice) {
            case "Calendar": {
                helpMessage = r.getString(R.string.help_calendar);
                break;
            }
            case "Groups": {
                helpMessage = r.getString(R.string.help_groups);
                break;
            }
            case "Invitations": {
                helpMessage = r.getString(R.string.help_invitations);
                break;
            }
            case "Settings": {
                helpMessage = r.getString(R.string.help_settings);
                break;
            }

            default:
                break;
        }

        if (item.getItemId() == R.id.help) {
            //Crear Popup amb l'ajuda d'aquesta activity
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(helpMessage);
            builder.setNeutralButton(r.getString(R.string.close), (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog alert = builder.create();
            alert.setTitle(r.getString(R.string.help));
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Context c = LocaleLanguage.getLocale(this);
        Resources r = c.getResources();
        switch (item.getItemId()) {
            case R.id.navCalendar: {
                changeFragment(new CalendarFragment(), true);
                choice = "Calendar";
                break;
            }
            case R.id.navGroups: {
                changeFragment(new GroupsFragment(), true);
                choice = "Groups";
                break;
            }
            case R.id.navInvitations: {
                changeFragment(new InvitationsFragment(), true);
                choice = "Invitations";
                break;
            }
            case R.id.navSettings: {
                changeFragment(new SettingsFragment(), true);
                choice = "Settings";
                break;
            }
            case R.id.navLogOut: {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_log_out, null, false), 900, 400, true);
                pw.setBackgroundDrawable(getDrawable(ThemeSwitcher.lightThemeSelected() ? R.drawable.rounded_corners : R.drawable.rounded_corners_dark));
                pw.showAtLocation(this.findViewById(R.id.fragment_container), Gravity.CENTER, 0, 0);

                ((TextView)pw.getContentView().findViewById(R.id.logOutTitle)).setText(r.getString(R.string.log_out));
                ((TextView)pw.getContentView().findViewById(R.id.logOutDescription)).setText(r.getString(R.string.are_you_sure_you_want_to_log_out));
                Button cancelBtn = pw.getContentView().findViewById(R.id.cancelBtn);
                cancelBtn.setText(r.getString(R.string.cancel));
                cancelBtn.setOnClickListener(view -> pw.dismiss());

                Button yesBtn = pw.getContentView().findViewById(R.id.yesBtn);
                yesBtn.setText(r.getString(R.string.yes));
                yesBtn.setOnClickListener(view -> {
                    DataBaseAdapter.logOut();
                    pw.dismiss();
                    Intent i = new Intent(NavigationDrawer.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                });
                return true;
            }
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateProfilePicture() {
        Task<byte[]> task = DataBaseAdapter.updateProfilePicture();
        task.addOnCompleteListener(task1 -> {
            byte[] byteArray = DataBaseAdapter.getByteArray();
            profilePictureND.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        });
    }

    public void changeFragment(Fragment fragment, boolean compulsory) {
        //System.out.println(fragment);
        //System.out.println(PrevFragment.getPrevFragment());
        //if (!compulsory) fragment = PrevFragment.getPrevFragment();
        //PrevFragment.setPrevFragment(fragment);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeLanguage() {
        Menu menu = navigationView.getMenu();
        Resources r = LocaleLanguage.getLocale(this).getResources();

        String title;
        switch(choice) {
            case "Calendar": {
                title = r.getString(R.string.calendar);
                break;
            }
            case "Groups": {
                title = r.getString(R.string.groups);
                break;
            }
            case "Invitations": {
                title = r.getString(R.string.groups);
                break;
            }
            case "Settings": {
                title = r.getString(R.string.settings);
                break;
            }
            default:
                title = "";
                break;
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

        menu.getItem(0).setTitle(r.getString(R.string.calendar));
        menu.getItem(1).setTitle(r.getString(R.string.groups));
        menu.getItem(2).setTitle(r.getString(R.string.invitations));
        menu.getItem(3).setTitle(r.getString(R.string.settings));
        menu.getItem(4).setTitle(r.getString(R.string.log_out));
    }

    //Fer que no es faci res quan s'apreta el back button que proporciona Android per defecte,
    //abans et portava a la pantalla de login sense fer logoff.
    @Override
    public void onBackPressed() {

    }
}