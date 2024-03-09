package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.GoalFormatter;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class GoalFormatterTest {
    @Test
    public void recurGoalText() throws ParseException {
        GoalFormatter goalFormatter = new GoalFormatter();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        Calendar currDate = Calendar.getInstance();
        currDate.setTime(sdf.parse("Mar 1 09:00:00 AM 2024"));

        assertEquals(goalFormatter.recurGoalText(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY),
                "Goal 1", Goal.RecurrencePattern.DAILY), "Goal 1, Daily");
        assertEquals(goalFormatter.recurGoalText(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY),
                "Goal 1", Goal.RecurrencePattern.WEEKLY), "Goal 1, Weekly on Fri");
        assertEquals(goalFormatter.recurGoalText(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY),
                "Goal 1", Goal.RecurrencePattern.MONTHLY), "Goal 1, Monthly on 1st Fri");
        assertEquals(goalFormatter.recurGoalText(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY),
                "Goal 1", Goal.RecurrencePattern.YEARLY), "Goal 1, Yearly on 3/01");
    }
}
