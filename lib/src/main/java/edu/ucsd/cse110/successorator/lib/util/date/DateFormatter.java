package edu.ucsd.cse110.successorator.lib.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormatter {
    public String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE M/dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
