package edu.ucsd.cse110.successorator.app.util;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Converters {
    @TypeConverter
    public static String fromCalendar(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return calendar == null ? null : dateFormat.format(calendar.getTime());
    }

    @TypeConverter
    public static Calendar toCalendar(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(dateString));
        } catch (Exception e) {
            // Handle the exception if the date string is not in the expected format
            e.printStackTrace();
        }
        return calendar;
    }
}
