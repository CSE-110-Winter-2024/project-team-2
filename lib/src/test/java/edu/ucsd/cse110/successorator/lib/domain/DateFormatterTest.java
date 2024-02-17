package edu.ucsd.cse110.successorator.lib.domain;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.lib.util.date.DateFormatter;

public class DateFormatterTest {
    /**
     * Tests to make sure that the format of the date is correct when running the app
     */
    @Test
    public void formatDateTest() {
        DateFormatter dateFormatter = new DateFormatter();
        Calendar fakedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        String formattedDate = dateFormatter.formatDateActionBar(fakedCalendar);
        assert (formattedDate.equals("Monday 2/12"));
    }
}
