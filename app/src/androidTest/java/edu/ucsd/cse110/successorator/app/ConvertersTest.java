package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.app.util.Converters;

/**
 * Tests the methods of the Converters utility class
 */
public class ConvertersTest {
    @Test
    public void fromCalendar() {
        //Converters converter = new Converters();
        Calendar date = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        String expected = "2024-02-12";
        String actual = new Converters().fromCalendar(date);
        assertEquals(expected,actual);
    }

    @Test
    public void toCalendar() {
        String strDate = "2024-02-12";
        Calendar expected = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        Calendar actual = new Converters().toCalendar(strDate);
        assertEquals(expected, actual);
    }
}
