package edu.ucsd.cse110.successorator.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


@RunWith(AndroidJUnit4.class)
public class DateProviderTest {
    /**
     * Tests to make sure that the format of the date is correct when running the app
     */
    @Test
    public void formattedDate() {
        Calendar fakedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 12);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE M/dd", Locale.getDefault());
        String formattedDate = dateFormat.format(fakedCalendar.getTime());
        assert (formattedDate.equals("Monday 2/12"));
    }

}
