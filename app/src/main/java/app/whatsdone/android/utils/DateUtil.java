package app.whatsdone.android.utils;

import java.util.Date;

public class DateUtil {
    public static Date getLastMinuteDate(Date date){
        Long time = date.getTime();
        Date newDate = new Date(time + time % (24 * 60 * 60 * 1000) - 1);
        return newDate;
    }

}
