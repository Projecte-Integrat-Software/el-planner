package com.example.our_planner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public abstract class ThemeSwitcher {

    private static final String LIGHT_THEME = "LightTheme";
    private static boolean lightSelected;

    public static boolean lightThemeSelected() {
        return lightSelected;
    }

    public static void saveTheme(Context context, boolean light) {
        lightSelected = light;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(LIGHT_THEME, light);
        editor.apply();
        updateTheme();
    }

    public static void updateTheme() {
        AppCompatDelegate.setDefaultNightMode(lightSelected ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }
}
