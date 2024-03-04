package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateComparer {
    /**
     * This method compares two given Calendar dates and returns true if
     * firstDate is a date before secondDate and returns false otherwise.
     * Compares the year, month, and days only. Does not compare time.
     * @param firstDate the firstDate being compared
     * @param secondDate the date firstDate is compared to
     * @return whether or not firstDate is before secondDate
     */
    public boolean isFirstDateBeforeSecondDate(Calendar firstDate, Calendar secondDate) {
        // Create Calendar instances of firstDate and secondDate, but
        // disregard the time by setting them to midnight
        Calendar firstDateCopy = new GregorianCalendar(firstDate.get(Calendar.YEAR),
                firstDate.get(Calendar.MONTH),
                firstDate.get(Calendar.DAY_OF_MONTH));
        Calendar secondDateCopy = new GregorianCalendar(secondDate.get(Calendar.YEAR),
                secondDate.get(Calendar.MONTH),
                secondDate.get(Calendar.DAY_OF_MONTH));
        return firstDateCopy.compareTo(secondDateCopy) < 0;
    }
}
