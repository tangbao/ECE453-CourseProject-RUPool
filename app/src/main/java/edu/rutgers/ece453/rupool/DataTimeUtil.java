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

        //mm/dd/yyyy

        result = ddmmyy[2] + ddmmyy[0] + ddmmyy[1] + "T10:00:00-07:00";


        return result;
    }

    static String getMonth(String date){
        String result = "";

        String[] ddmmyy = date.split("/");

        switch (ddmmyy[0]){
            case "01":
                result = "Jan";
                break;
            case "02":
                result = "Feb";
                break;
            case "03":
                result = "Mar";
                break;
            case "04":
                result = "Apr";
                break;
            case "05":
                result = "May";
                break;
            case "06":
                result = "Jun";
                break;
            case "07":
                result = "Jul";
                break;
            case "08":
                result = "Aug";
                break;
            case "09":
                result = "Sep";
                break;
            case "10":
                result = "Oct";
                break;
            case "11":
                result = "Nov";
                break;
            case "12":
                result = "Dec";
                break;
        }

        return result;
    }

    static String getDay(String date){
        String[] ddmmyy = date.split("/");

        return ddmmyy[1];
    }

}
