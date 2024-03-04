package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;

public class DateComparerTest {
    @Test
    public void isFirstDateBeforeSecondDate() {
        // Test first date is before second date
        Calendar firstDate = new GregorianCalendar(2024, Calendar.MARCH, 2, 1, 30);
        Calendar secondDate = new GregorianCalendar(2025, Calendar.FEBRUARY, 3, 2, 0);
        assertTrue(new DateComparer().compareDates(firstDate, secondDate) < 0);

        // Test same day, different times
        firstDate = new GregorianCalendar(2024, Calendar.MARCH, 2, 1, 30);
        secondDate = new GregorianCalendar(2024, Calendar.MARCH, 2, 5, 0);
        assertEquals(new DateComparer().compareDates(firstDate, secondDate), 0);

        // Test first date is after second date
        firstDate = new GregorianCalendar(2024, Calendar.MARCH, 3);
        secondDate = new GregorianCalendar(2024, Calendar.MARCH, 2);
        assertTrue(new DateComparer().compareDates(firstDate, secondDate) > 0);
    }
}
