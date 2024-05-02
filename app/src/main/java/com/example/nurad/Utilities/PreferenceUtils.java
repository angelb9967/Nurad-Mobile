package com.example.nurad.Utilities;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
public class PreferenceUtils {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_USERS = "users";

    public static void saveUser(Context context, String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Set<String> userSet = sharedPreferences.getStringSet(KEY_USERS, new HashSet<>());
        String userData = email + "|" + password;
        userSet.add(userData);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_USERS, userSet);
        editor.apply();
    }

    public static Set<String> getSavedEmails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Set<String> userSet = sharedPreferences.getStringSet(KEY_USERS, new HashSet<>());
        Set<String> emails = new HashSet<>();
        for (String userData : userSet) {
            String[] parts = userData.split("\\|");
            emails.add(parts[0]);
        }
        return emails;
    }

    public static String getPasswordForEmail(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Set<String> userSet = sharedPreferences.getStringSet(KEY_USERS, new HashSet<>());
        for (String userData : userSet) {
            String[] parts = userData.split("\\|");
            if (parts.length == 2 && parts[0].equals(email)) {
                return parts[1];
            }
        }
        return null;
    }

    public static void clearSavedUsers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERS);
        editor.apply();
    }
}

