package edu.ucsd.cse110.successorator.lib.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormatter {
    public String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE M/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public String formatWeekDay(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public String formatDayOfMonth(Calendar calendar){
        SimpleDateFormat dayOfWeekInMonthFormat = new SimpleDateFormat("F", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        String day = dayFormat.format(calendar.getTime());
        String dayOfWeekInMonth = dayOfWeekInMonthFormat.format(calendar.getTime());

        String suffix;
        switch (dayOfWeekInMonth) {
            case "1":  // determine the suffix based on day of Week in month
                suffix = "st";
                break;
            case "2":
                suffix = "nd";
                break;
            case "3":
                suffix = "rd";
                break;
            default:
                suffix = "th";
                break;
        }
        return String.format("%s%s %s", dayOfWeekInMonth, suffix, day);
    }

    public String formatDayOfYear(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
