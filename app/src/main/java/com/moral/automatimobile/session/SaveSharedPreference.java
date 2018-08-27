package com.moral.automatimobile.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.serializer.ObjectSerializer;

import java.io.IOException;

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Set login status
    public static void setLoggedIn(Context context, boolean loggedIn, String email) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean("logged_in", loggedIn);
        editor.putString("User Email", email);
        editor.apply();
    }

    public static void setNewCarProperties(Context context, Color color, Transmission transmission, Engine engine) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        try {
            editor.putString("Car", "New");
            editor.putString("New_Car_Color", ObjectSerializer.serialize(color));
            editor.putString("New_Car_Transmission", ObjectSerializer.serialize(transmission));
            editor.putString("New_Car_Engine", ObjectSerializer.serialize(engine));
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUsedCar(Context context, Car car) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        try {
            editor.putString("Car", "Used");
            editor.putString("Used_Car", ObjectSerializer.serialize(car));
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get login status
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean("logged_in", false);
    }

    // get user email
    public static String getEmail(Context context) {
        return getPreferences(context).getString("User Email", "none");
    }

    // get color
    public static String getColor(Context context) {
        return getPreferences(context).getString("New_Car_Color", "no color found");
    }

    // get transmission
    public static String getTransmission(Context context) {
        return getPreferences(context).getString("New_Car_Transmission", "no transmission found");
    }

    // get engine
    public static String getEngine(Context context) {
        return getPreferences(context).getString("New_Car_Engine", "no engine found");
    }

    public static String getUsedCar(Context context) {
        return getPreferences(context).getString("Used_Car", "No used car found");
    }
}
