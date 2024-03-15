package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Class for mock date provider that always returns the same date no matter what, used for testing
 */
public class MockDateProvider implements DateProvider {
    private final Calendar calendar;

    public MockDateProvider(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Calendar getCurrentDate() {
        return calendar;
    }

    @Override
    public Calendar getCurrentViewDate(ViewOptions view) {
        Calendar date = (Calendar) calendar.clone();

        // Set our date 2 hours back
        date.add(Calendar.HOUR_OF_DAY, -2);

        // Advance our date if we are on Tomorrow's view
        if (view == ViewOptions.TOMORROW) {
            date.add(Calendar.DATE,1);
        }

        return date;
    }
}
