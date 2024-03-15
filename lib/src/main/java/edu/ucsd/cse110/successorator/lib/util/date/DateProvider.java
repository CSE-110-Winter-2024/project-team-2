package edu.ucsd.cse110.successorator.lib.util.date;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Interface for Providing the current date
 */
public interface DateProvider {
    Calendar getCurrentDate();

    Calendar getCurrentViewDate(ViewOptions view);
}
