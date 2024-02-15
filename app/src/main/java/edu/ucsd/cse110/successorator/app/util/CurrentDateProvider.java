package edu.ucsd.cse110.successorator.app.util;

import java.util.Calendar;

/**
 * Class for implementation of getting the current date
 */
public class CurrentDateProvider implements DateProvider{
    @Override
    public Calendar getCurrentDate(){
        return Calendar.getInstance();
    }

    // set time back 2 hours to ensure date is from previous day until 2am
    public Calendar setTwoHoursBack(Calendar calendar) {
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        return calendar;
    }
}
