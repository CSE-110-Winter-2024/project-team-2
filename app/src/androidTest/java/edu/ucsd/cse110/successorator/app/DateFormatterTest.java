package edu.ucsd.cse110.successorator.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.app.util.DateFormatter;

@RunWith(AndroidJUnit4.class)
public class DateFormatterTest {
    /**
     * Tests to make sure that the format of the date is correct when running the app
     */
    @Test
    public void formatDateTest() {
        DateFormatter dateFormatter = new DateFormatter();
        Calendar fakedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        String formattedDate = dateFormatter.formatDate(fakedCalendar);
        assert (formattedDate.equals("Monday 2/12"));
    }
}
