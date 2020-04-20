package com.example.cityweather.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public static Date getShortDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNormalDate(Date date) {
        return new SimpleDateFormat("EEEE DD yyyy").format(date);
    }

    public static String getHours(Date date) {
        return new SimpleDateFormat("hh a").format(date);
    }

    private String getDateString(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

}
