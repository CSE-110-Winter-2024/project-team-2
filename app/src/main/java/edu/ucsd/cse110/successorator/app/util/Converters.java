package edu.ucsd.cse110.successorator.app.util;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * Class to convert Calendar object to string and vice versa when working with
 * calendar value in database
 */
public class Converters {
    @TypeConverter
    public static String fromCalendar(Calendar calendar) {
        if(calendar == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return calendar == null ? null : dateFormat.format(calendar.getTime());
    }

    @TypeConverter
    public static Calendar toCalendar(String dateString) {
        if(dateString == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(dateFormat.parse(dateString)));
        } catch (Exception e) {
            // Handle the exception if the date string is not in the expected format
            e.printStackTrace();
        }
        return calendar;
    }
}
