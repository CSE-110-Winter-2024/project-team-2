package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;

/**
 * Class for implementation of getting the current date
 */
public class CurrentDateProvider implements DateProvider {
    @Override
    public Calendar getCurrentDate() {
        return Calendar.getInstance();
    }
}
