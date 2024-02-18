package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;

/**
 * Class for mock date provider that always returns the same date no matter what, used for testing
 */
public class MockDateProvider implements DateProvider {
    private final Calendar calendar;

    public MockDateProvider(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Calendar getCurrentDate() {
        return calendar;
    }
}
