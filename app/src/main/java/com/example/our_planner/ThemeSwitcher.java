package com.example.our_planner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public abstract class ThemeSwitcher {

    private static final String LIGHT_THEME = "LightTheme";

    public static boolean lightThemeSelected(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LIGHT_THEME, true);
    }

    public static void saveTheme(Context context, boolean light) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(LIGHT_THEME, light);
        editor.apply();
        changeTheme(light);
    }

    private static void changeTheme(boolean light) {
        AppCompatDelegate.setDefaultNightMode(light ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static void updateTheme(Context context) {
        changeTheme(lightThemeSelected(context));
    }
}
