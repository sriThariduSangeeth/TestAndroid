package app.whatsdone.android.utils;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthServiceImpl;

public class GetCurrentDetails {

    public Date getCurrentDateTime (){

        Date date = null;
        Date today = Calendar.getInstance().getTime();

        // Constructs a SimpleDateFormat using the given pattern
        SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz", Locale.getDefault());

        // format() formats a Date into a date/time string.
        String currentTime = crunchifyFormat.format(today);

        try {

            // parse() parses text from the beginning of the given string to produce a date.
            date = crunchifyFormat.parse(currentTime);

            // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
            //epochTime = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public User getCurrentUser(){
        return AuthServiceImpl.getCurrentUser();
    }

}