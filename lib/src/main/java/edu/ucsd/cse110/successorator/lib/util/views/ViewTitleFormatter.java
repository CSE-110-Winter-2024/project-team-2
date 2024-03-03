package edu.ucsd.cse110.successorator.lib.util.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.ucsd.cse110.successorator.lib.util.date.DateFormatter;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Provides the formatted heading at the top of the screen
 */
public class ViewTitleFormatter {
    public String formatViewTitle(ViewOptions view, Calendar day) {
        if (view == ViewOptions.TODAY) {
            return "Today, " + new DateFormatter().formatDate(day);
        } else if (view == ViewOptions.TOMORROW) {
            return "Tomorrow, " + new DateFormatter().formatDate(day);
        } else if (view == ViewOptions.PENDING) {
            return "Pending";
        } else if (view == ViewOptions.RECURRING) {
            return "Recurring";
        }
        return "";
    }
}
