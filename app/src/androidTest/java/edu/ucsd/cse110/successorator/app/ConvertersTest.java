package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.app.util.Converters;

/**
 * Tests the methods of the Converters utility class
 */
public class ConvertersTest {
    @Test
    public void fromCalendar() {
        long expected = 1707724800L;
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(expected);
        long actual = Converters.fromCalendar(date);
        assertEquals(expected, actual);

        // 0 should convert to null
        expected = 0;
        date = null;
        actual = Converters.fromCalendar(date);
        assertEquals(expected, actual);
    }

    @Test
    public void toCalendar() {
        long timestampMillis = 1707724800L;
        Calendar expected = Calendar.getInstance();
        expected.setTimeInMillis(timestampMillis);
        Calendar actual = Converters.toCalendar(timestampMillis);
        assertEquals(expected, actual);

        // null should convert to 0
        timestampMillis = 0;
        expected = null;
        actual = Converters.toCalendar(timestampMillis);
        assertEquals(expected, actual);
    }
}
