package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Class for implementation of getting the current date
 */
public class CurrentDateProvider implements DateProvider {
    @Override
    public Calendar getCurrentDate() {
        return Calendar.getInstance();
    }

    @Override
    public Calendar getCurrentViewDate(ViewOptions view) {
        Calendar date = getCurrentDate();
        if (date == null) {
            return null;
        }

        // Set our date 2 hours back
        date.add(Calendar.HOUR_OF_DAY, -2);

        // Advance our date if we are on Tomorrow's view
        if (view == ViewOptions.TOMORROW) {
            date.add(Calendar.DATE,1);
        }

        return date;
    }
}
