package edu.rutgers.ece453.rupool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * Created by zhu_z on 2017/12/16.
 *
 * Convert by zhongze tang
 */

public class DataTimeUtil {

    static String dateForDateTime(String date) {
        String result = "";

        String[] ddmmyy = date.split("/");

        //dd/mm/yyyy

        result = ddmmyy[2] + ddmmyy[0] + ddmmyy[1] + "T10:00:00-07:00";


        return result;
    }

}
