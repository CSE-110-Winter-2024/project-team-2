package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateProviderTest {
    /**
     * Tests to make sure that the format of the date is correct when running the app
     */
    @Test
    public void correctDateTest() {
        Calendar fakedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE M/dd", Locale.getDefault());
        String formattedDate = dateFormat.format(fakedCalendar.getTime());
        assertEquals("Monday 2/12", formattedDate);
    }

    /**
     * Tests for setTwoHoursBack method over 24 hour period
     */
    @Test
    public void setTwoHoursBackTest() {
        int expectedDay;

        // Iterate through every hour and minute over 24 hour period
        for (int hourOfDay = 0; hourOfDay < 24; hourOfDay++) {
            for (int minute = 0; minute < 60; minute++) {
                //set values of actual calendar
                Calendar actual = new GregorianCalendar(2024, Calendar.FEBRUARY, 12, hourOfDay, minute );
                int expectedHourOfDay = hourOfDay - 2;

                if (hourOfDay <= 1) {
                    //if hourOfDay is less than 1, expected day is previous day
                    // and expectedHourOfDay needs to be converted to 24 hour scale
                    expectedHourOfDay += 24;
                    expectedDay = 11;
                } else {
                    // if hourOfDay is greater than 1, expected day is same day
                    expectedDay = 12;
                }

                // set values of expected calendar
                Calendar expected = new GregorianCalendar(2024, Calendar.FEBRUARY, expectedDay, expectedHourOfDay, minute );

                actual.add(Calendar.HOUR_OF_DAY, -2);
                assert (actual.equals(expected));
            }
        }
    }
}
