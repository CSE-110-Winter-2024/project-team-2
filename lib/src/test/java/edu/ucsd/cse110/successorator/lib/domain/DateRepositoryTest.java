package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.lib.util.date.DateProvider;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;

public class DateRepositoryTest {
    private int observeCallsMade = 0;
    private Calendar lastObservedDate = null;

    @Test
    public void advanceDateForward() {
        Calendar calendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 14);
        DateProvider dateProvider = new MockDateProvider(calendar);
        DateRepository dateRepository = new DateRepository(dateProvider);
        assertEquals(dateRepository.getDate().getValue(), calendar);
        dateRepository.getDate().observe(newDate -> {
            this.observeCallsMade++;
            lastObservedDate = newDate;
        });
        assertEquals(lastObservedDate, calendar);
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        dateRepository.advanceDateOneDayForward();
        Calendar advancedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 15);
        assertEquals(dateRepository.getDate().getValue(), advancedCalendar);
        assertEquals(lastObservedDate, advancedCalendar);
        assertEquals(observeCallsMade, 2);

        dateRepository.advanceDateOneDayForward();
        advancedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 16);
        assertEquals(dateRepository.getDate().getValue(), advancedCalendar);
        assertEquals(lastObservedDate, advancedCalendar);
        assertEquals(observeCallsMade, 3);
    }
}
