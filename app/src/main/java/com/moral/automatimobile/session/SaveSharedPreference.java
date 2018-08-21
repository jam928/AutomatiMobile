package com.moral.automatimobile.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Set login status
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean("logged_in", loggedIn);
        editor.apply();
    }

    // get login status
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean("logged_in", false);
    }
}
