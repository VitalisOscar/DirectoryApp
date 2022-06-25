package com.directoryapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.directoryapp.models.User;

import java.util.ArrayList;

public class AuthHelper {
    static final String LOGGED_IN = "logged_in";

    static final String PREFERENCES = "auth";

    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String TIMESTAMP = "timestamp";

    public static boolean isLoggedIn(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(LOGGED_IN, false);
    }

    public static void logout(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveUser(Context context, User user){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putBoolean(LOGGED_IN, true);

            editor.putString(ID, user.getId());
            editor.putString(UID, user.getId());
            editor.putString(NAME, user.getName());
            editor.putString(EMAIL, user.getEmail());
            editor.putString(PHONE, user.getPhone());
            editor.putString(TIMESTAMP, user.getTimestamp());

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getCurrentUser(Context context){
        if(!isLoggedIn(context)) return null;

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        return new User(
            preferences.getString(NAME, null),
            preferences.getString(EMAIL, null),
            preferences.getString(PHONE, null),
            new ArrayList<>(),
            preferences.getString(ID, null),
            preferences.getString(TIMESTAMP, null)
        );
    }
}
