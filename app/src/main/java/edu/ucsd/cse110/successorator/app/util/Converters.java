package edu.ucsd.cse110.successorator.app.util;

import androidx.room.TypeConverter;

import java.util.Calendar;

/**
 * Class to convert Calendar object to string and vice versa when working with
 * calendar value in database
 */
public class Converters {
    @TypeConverter
    public static long fromCalendar(Calendar calendar) {
        if (calendar == null) {
            return 0;
        }
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public static Calendar toCalendar(long millis) {
        // Use a timesatmp of 0 (which represents 1/1/1970) to represent a null date
        if (millis == 0) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
}
