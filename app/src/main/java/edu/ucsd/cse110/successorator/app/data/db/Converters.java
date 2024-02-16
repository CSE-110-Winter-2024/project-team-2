package edu.ucsd.cse110.successorator.app.data.db;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Converters {
    @TypeConverter
    public static String fromCalendar(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE M/dd");
        return calendar == null ? null : dateFormat.format(calendar.getTime());
    }

    @TypeConverter
    public static Calendar toCalendar(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE M/dd");
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

