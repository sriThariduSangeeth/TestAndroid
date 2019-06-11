package app.whatsdone.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import app.whatsdone.android.WhatsDoneApplication;

public class SharedPreferencesUtil {
    public static void saveString(String key, String value){
        try {
            SharedPreferences sharedPref = WhatsDoneApplication.getApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.apply();
        }catch (Exception ex){
            Log.d("SHARED", ex.getLocalizedMessage());
        }

    }

    public static String getString(String key){
        try {
            SharedPreferences sharedPref = WhatsDoneApplication.getApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
            return sharedPref.getString(key, "");
        }catch (Exception ex){
            return "";
        }

    }
}
