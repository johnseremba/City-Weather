package com.example.cityweather.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public static Date getShortDate(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return simpleDateFormat.parse(getDateString(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNormalDate(Date date) {
        return new SimpleDateFormat("EEEE dd MMMM yyyy").format(date);
    }

    public static String getHours(Date date) {
        return new SimpleDateFormat("hh a").format(date);
    }

    public static String getDateString(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public static String getDayString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        return simpleDateFormat.format(date).toUpperCase();
    }
}
