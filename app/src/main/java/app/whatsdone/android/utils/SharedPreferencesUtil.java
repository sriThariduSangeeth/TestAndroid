package app.whatsdone.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.WhatsDoneApplication;
import timber.log.Timber;

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

    public static void save(String key, HashMap data){
        try {
            SharedPreferences sharedPref = WhatsDoneApplication.getApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String value = serialize(data, false);

            editor.putString(key, value);
            editor.apply();
        }catch (Exception ex){
            Timber.d(ex.getLocalizedMessage());
        }
    }

    public static HashMap get(String key){
        try {
            SharedPreferences sharedPref = WhatsDoneApplication.getApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
            String data = sharedPref.getString(key, "");
            return desSerialize(data);
        }catch (Exception ex){
            return null;
        }

    }

    private static  String serialize(Object obj, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        String output = "";
        try {

            if (pretty) {
                output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }else {
                output = mapper.writeValueAsString(obj);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return output;
    }

    private static HashMap desSerialize(String data) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        HashMap obj = null;

            try {
                obj = mapper.readValue(data, HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return obj;
    }

}
