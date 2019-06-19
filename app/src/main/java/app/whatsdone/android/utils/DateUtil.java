package app.whatsdone.android.utils;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
    public static Date getLastMinuteDate(Date date){
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23); //anything 0 - 23
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Date newDate = c.getTime();

        return newDate;
    }

    public static boolean isDateEqual(Date date1, Date date2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static boolean isDateTimeEqual(Date date1, Date date2){
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz", Locale.getDefault());
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date addTime(Date date, int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date removeDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String formatted(Date dueDate, @Nullable String format) {
        if(format == null)
            format = Constants.DATE_FORMAT;
        SimpleDateFormat fmt = new SimpleDateFormat(format, Locale.getDefault());
        return fmt.format(dueDate);
    }

    public static Date parse(String dateStr, @Nullable String format) {
        if(format == null)
            format = Constants.DATE_FORMAT;
        SimpleDateFormat fmt = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }
}
