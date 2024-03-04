package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateComparer {
    /**
     * This method compares two given Calendar dates by year, month, and days only (not considering
     * time) and returns the result of the comparison:
     * - a positive number if firstDate is after secondDate
     * - 0 if firstDate is the same as secondDate
     * - a negative number if firstDate is before secondDate
     *
     * @param firstDate the firstDate being compared
     * @param secondDate the date firstDate is compared to
     * @return the result of the date comparison
     */
    public int compareDates(Calendar firstDate, Calendar secondDate) {
        // Create Calendar instances of firstDate and secondDate, but
        // disregard the time by setting them to midnight
        Calendar firstDateCopy = new GregorianCalendar(firstDate.get(Calendar.YEAR),
                firstDate.get(Calendar.MONTH),
                firstDate.get(Calendar.DAY_OF_MONTH));
        Calendar secondDateCopy = new GregorianCalendar(secondDate.get(Calendar.YEAR),
                secondDate.get(Calendar.MONTH),
                secondDate.get(Calendar.DAY_OF_MONTH));
        return firstDateCopy.compareTo(secondDateCopy);
    }
}
