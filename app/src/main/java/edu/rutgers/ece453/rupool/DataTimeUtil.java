package edu.rutgers.ece453.rupool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zhu_z on 2017/12/16.
 */

public class DataTimeUtil {

    static String dateToISO8601(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear - 1, dayOfMonth, hourOfDay, minute, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("EST"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        String ret;
        ret = simpleDateFormat.format(calendar.getTime());
        return ret;
    }

}
