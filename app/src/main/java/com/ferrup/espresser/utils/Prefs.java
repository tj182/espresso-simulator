package com.ferrup.espresser.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String SETTINGS_PREFS = "SETTINGS_PREFS";
    private static final String SETTINGS_EMPLOYEES_COUNT = "SETTINGS_EMPLOYEES_COUNT";
    private static final String SETTINGS_CHANSE_OF_SUPERBUSYNESS = "SETTINGS_CHANSE_OF_SUPERBUSYNESS";
    private static final String SETTINGS_PERIOD_OF_SUPERBUSYNESS = "SETTINGS_PERIOD_OF_SUPERBUSYNESS";
    private static final String SETTINGS_COFFEE_INTERVAL = "SETTINGS_COFFEE_INTERVAL";
    private static final String SETTINGS_COFFEE_INTERVAL_ERROR = "SETTINGS_COFFEE_INTERVAL_ERROR";
    private static final String SETTINGS_MAKING_TIME = "SETTINGS_MAKING_TIME";
    private static final String SETTINGS_OUTPUTS_COUNT = "SETTINGS_OUTPUTS_COUNT";

    private Context context;
    private SharedPreferences settingsPrefs;

    public Prefs(Context context) {
        this.context = context;
    }

    private SharedPreferences getSettingsPrefs() {
        if (settingsPrefs == null) {
            settingsPrefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE);
        }
        return settingsPrefs;
    }

    public int getEmployeesCount() {
        return getSettingsPrefs().getInt(SETTINGS_EMPLOYEES_COUNT, 100);
    }

    public void setEmployeesCount(int count) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_EMPLOYEES_COUNT, count);
        editor.apply();
    }

    public int getChanceOfSuperBusyness() {
        return getSettingsPrefs().getInt(SETTINGS_CHANSE_OF_SUPERBUSYNESS, 10);
    }

    public void setChanceOfSuperBusyness(int percentAtHour) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_CHANSE_OF_SUPERBUSYNESS, percentAtHour);
        editor.apply();
    }

    public int getPeriodOfSuperBusyness() {
        return getSettingsPrefs().getInt(SETTINGS_PERIOD_OF_SUPERBUSYNESS, 3);
    }

    public void setPeriodOfSuperBusyness(int hours) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_PERIOD_OF_SUPERBUSYNESS, hours);
        editor.apply();
    }

    public int getCoffeeInterval() {
        return getSettingsPrefs().getInt(SETTINGS_COFFEE_INTERVAL, 60);
    }

    public void setCoffeeInterval(int mins) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_COFFEE_INTERVAL, mins);
        editor.apply();
    }

    public int getCoffeeIntervalError() {
        return getSettingsPrefs().getInt(SETTINGS_COFFEE_INTERVAL_ERROR, 10);
    }

    public void setCoffeeIntervalError(int mins) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_COFFEE_INTERVAL_ERROR, mins);
        editor.apply();
    }

    public int getMakingTime() {
        return getSettingsPrefs().getInt(SETTINGS_MAKING_TIME, 30);
    }

    public void setMakingTime(int secs) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_MAKING_TIME, secs);
        editor.apply();
    }

    public int getOutputsCount() {
        return getSettingsPrefs().getInt(SETTINGS_OUTPUTS_COUNT, 1);
    }

    public void setOutputsCount(int count) {
        SharedPreferences.Editor editor = getSettingsPrefs().edit();
        editor.putInt(SETTINGS_OUTPUTS_COUNT, count);
        editor.apply();
    }
}
