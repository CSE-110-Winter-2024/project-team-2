package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Tests for the methods of the Goal class.
 */
public class GoalTest {
    @Test
    public void getGoal() {
        for (int i = 0; i < 100; i++) {
            String theGoal = "Test goal " + i;
            Goal goal = new Goal(0, theGoal, 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            assertEquals(goal.getGoalText(), theGoal);
        }
    }

    @Test
    public void getId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(i, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            assertEquals(Integer.valueOf(i), goal.getId());
        }
    }

    @Test
    public void getSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            assertEquals(Integer.valueOf(i), goal.getSortOrder());
        }
    }

    @Test
    public void getIsComplete() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertFalse(goal.getIsComplete());
    }

    @Test
    public void getDateCompleted() {
        Calendar dateCompleted = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, dateCompleted, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        dateCompleted.set(Calendar.HOUR, 0);
        dateCompleted.set(Calendar.MINUTE, 0);
        dateCompleted.set(Calendar.SECOND, 0);
        dateCompleted.set(Calendar.MILLISECOND, 0);
        dateCompleted.set(Calendar.AM_PM, Calendar.AM);
        assertEquals(dateCompleted, goal.getDateCompleted());
    }

    @Test
    public void getIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertTrue(goal.getIsDisplayed());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, false, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertFalse(goal2.getIsDisplayed());
    }

    @Test
    public void setIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertTrue(goal.getIsDisplayed());

        goal.setIsDisplayed(false);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void getGoalDate() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertEquals(currDate, goal.getGoalDate());
    }

    @Test
    public void getIsPending() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertFalse(goal.getIsPending());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, true, currDate, true, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertTrue(goal2.getIsPending());
    }

    @Test
    public void setGoalDate() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.setGoalDate(currDate);
        assertEquals(currDate, goal.goalDate);
    }

    @Test
    public void setIsPending() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.setIsPending(true);
        assertTrue(goal.isPending);
    }

    @Test
    public void changeIsCompleteStatus() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.changeIsCompleteStatus();
        assertTrue(goal.isComplete);
    }

    @Test
    public void updateIsDisplayed() {
        Calendar currDate = Calendar.getInstance();
        Calendar dateCompleted = Calendar.getInstance();
        Calendar yesterdayGoalDate = Calendar.getInstance();
        Calendar todayGoalDate = Calendar.getInstance();
        Calendar tommorowGoalDate = Calendar.getInstance();
        yesterdayGoalDate.add(Calendar.DAY_OF_MONTH, -1);
        tommorowGoalDate.add(Calendar.DAY_OF_MONTH, 1);
        Calendar tomorrowDate = Calendar.getInstance();
        tomorrowDate.add(Calendar.DAY_OF_MONTH, 1);
        Boolean isPending = true;

        // When not goal is not completed, TODAY view, goal made TODAY
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(todayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal made yesterday
        goal = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(yesterdayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal made TOMORROW
        Goal goal1 = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(tommorowGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal1.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(goal1.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal is PENDING
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, true, null, isPending, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal2.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(goal2.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made TOMORROW
        Goal goal3 = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(tommorowGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal3.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TOMORROW), ViewOptions.TOMORROW);
        assertTrue(goal3.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made TODAY
        goal3 = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(todayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal3.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TOMORROW), ViewOptions.TOMORROW);
        assertFalse(goal3.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made yesterday
        goal3 = new Goal(0, "test Goal", 0, false, null, true, new MockDateProvider(yesterdayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal3.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TOMORROW), ViewOptions.TOMORROW);
        assertFalse(goal3.getIsDisplayed());

        // Today list: Same day goal is crossed off
        Goal goal4 = new Goal(0, "test Goal", 0, true, dateCompleted, true, new MockDateProvider(todayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal4.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(goal4.getIsDisplayed());

        // Today list: Day after goal is crossed off
        currDate.add(Calendar.DATE, 1);
        Goal goal5 = new Goal(0, "test Goal", 0, true, dateCompleted, true, new MockDateProvider(todayGoalDate).getCurrentViewDate(ViewOptions.TODAY), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal5.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(goal5.getIsDisplayed());
    }

    /**
     * Edge cases for whether to display goals marked as complete. A goal should be display if either:
     * 1. It is not marked as complete
     * 2. It was marked as complete on or the "current day", with a "day" beginning at 2 AM.
     * Otherwise, the goal should not be displayed.
     */
    @Test
    public void updateIsDisplayedEdgeCases() throws ParseException {
        // Date format for parsing date strings
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        // Goal is marked as complete at 1 AM on 2/17
        Calendar dateCompleted = Calendar.getInstance();
        dateCompleted.setTime(sdf.parse("Feb 17 01:00:00 AM 2024"));

        // Goal should still be displayed at 1:59 AM
        Calendar currDate = Calendar.getInstance();
        currDate.setTime(sdf.parse("Feb 17 01:59:00 AM 2024"));
        Calendar goalDate = (Calendar) currDate.clone();
        Goal goal = new Goal(0, "test Goal", 0, true,
                new MockDateProvider(dateCompleted).getCurrentViewDate(ViewOptions.TODAY),
                true, new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.TODAY),
                false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM
        currDate.setTime(sdf.parse("Feb 17 02:01:00 AM 2024"));
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(goal.getIsDisplayed());

        // Goal is marked as complete at 11:00 PM on 2/17
        dateCompleted.setTime(sdf.parse("Feb 17 11:00:00 PM 2024"));
        goal.setDateCompleted(dateCompleted);

        // Goal should still be displayed at 1:30 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 01:30:00 AM 2024"));
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 02:01:00 AM 2024"));
        goal.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void updateIsDisplayedRecurringGoals() throws ParseException {
        // Date format for parsing date strings
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        Calendar currDate = Calendar.getInstance();
        currDate.setTime(sdf.parse("Mar 1 09:00:00 AM 2024"));

        GoalFactory goalFactory = new GoalFactory();

        var recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.DAILY);
        var recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.DAILY, null);

        // Recurring template should only be shown in recurring view
        recurTemplate.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.RECURRING), ViewOptions.RECURRING);
        assertTrue(recurTemplate.getIsDisplayed());
        recurTemplate.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.PENDING), ViewOptions.PENDING);
        assertFalse(recurTemplate.getIsDisplayed());
        recurTemplate.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertFalse(recurTemplate.getIsDisplayed());
        recurTemplate.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TOMORROW), ViewOptions.TOMORROW);
        assertFalse(recurTemplate.getIsDisplayed());

        // Recurring instance should initially be shown on the today view
        recurInstance.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(recurInstance.getIsDisplayed());

        // Recurring instance should still be shown the next day
        currDate.setTime(sdf.parse("Mar 2 09:00:00 AM 2024"));
        recurInstance.updateIsDisplayed(new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY);
        assertTrue(recurInstance.getIsDisplayed());
    }

    @Test
    public void getNextRecurrenceDate() throws ParseException {
        // Date format for parsing date strings
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        Calendar currDate = Calendar.getInstance();
        currDate.setTime(sdf.parse("Mar 1 09:00:00 AM 2024"));

        GoalFactory goalFactory = new GoalFactory();

        // Daily
        var recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.DAILY);
        var recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.DAILY, null);
        Calendar expected = Calendar.getInstance();
        expected.setTime(sdf.parse("Mar 2 09:00:00 AM 2024"));
        // Use DateComparer to ensure goals are on the same day
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Weekly
        recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.WEEKLY);
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.WEEKLY, null);
        expected.setTime(sdf.parse("Mar 8 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Monthly
        recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.MONTHLY);
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.MONTHLY, null);
        expected.setTime(sdf.parse("Apr 5 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Yearly
        recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.YEARLY);
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);
        expected.setTime(sdf.parse("Mar 1 09:00:00 AM 2025"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);
    }

    @Test
    public void getNextRecurrenceDateMonthlyEdgeCases() throws ParseException {
        // Date format for parsing date strings
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        GoalFactory goalFactory = new GoalFactory();
        Calendar currDate = Calendar.getInstance();
        var expected = Calendar.getInstance();

        // 5th Friday of March -> next should be 1st Friday of May
        currDate.setTime(sdf.parse("Mar 29 09:00:00 AM 2024"));

        var recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.MONTHLY);
        var recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.MONTHLY, null);

        expected.setTime(sdf.parse("May 3 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Next should be 5th Friday of May
        currDate.setTime(expected.getTime());
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.MONTHLY, null);
        expected.setTime(sdf.parse("May 31 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);
    }

    @Test
    public void getNextRecurrenceDateYearlyEdgeCases() throws ParseException {
        // Date format for parsing date strings
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh:mm:ss a yyyy", Locale.ENGLISH);

        GoalFactory goalFactory = new GoalFactory();
        Calendar currDate = Calendar.getInstance();
        var expected = Calendar.getInstance();

        // Leap day 2024 -> next should be March 1, 2025 because 2025 is not a leap year
        currDate.setTime(sdf.parse("Feb 29 09:00:00 AM 2024"));

        var recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.YEARLY);
        var recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);

        expected.setTime(sdf.parse("Mar 1 09:00:00 AM 2025"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Next should be March 1, 2026
        currDate.setTime(expected.getTime());
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);
        expected.setTime(sdf.parse("Mar 1 09:00:00 AM 2026"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Next should be March 1, 2027
        currDate.setTime(expected.getTime());
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);
        expected.setTime(sdf.parse("Mar 1 09:00:00 AM 2027"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // Next should be Feb 29, 2028
        currDate.setTime(expected.getTime());
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);
        expected.setTime(sdf.parse("Feb 29 09:00:00 AM 2028"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);


        // Feb 28, 2023 -> next should be Feb 28, 2024
        currDate.setTime(sdf.parse("Feb 28 09:00:00 AM 2023"));

        recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.YEARLY);
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);

        expected.setTime(sdf.parse("Feb 28 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);

        // March 1, 2023 -> next should be March 1, 2024
        currDate.setTime(sdf.parse("Mar 1 09:00:00 AM 2023"));

        recurTemplate = goalFactory.makeRecurringTemplate("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), ViewOptions.TODAY, 1, Goal.RecurrencePattern.YEARLY);
        recurInstance = goalFactory.makeRecurringInstance("Goal 1", new MockDateProvider(currDate).getCurrentViewDate(ViewOptions.TODAY), 1, Goal.RecurrencePattern.YEARLY, null);

        expected.setTime(sdf.parse("Mar 1 09:00:00 AM 2024"));
        assertEquals(new DateComparer().compareDates(recurInstance.calculateNextRecurrenceDate(recurTemplate), expected), 0);
    }

    @Test
    public void withSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal actual = goal.withSortOrder(i);
            assertEquals(Integer.valueOf(i), actual.getSortOrder());
        }
    }

    @Test
    public void withId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal actual = goal.withId(i);
            assertEquals(Integer.valueOf(i), actual.getId());
        }
    }

    @Test
    public void testEquals() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            assertEquals(goal1, goal2);
            assertNotEquals(goal1, goal3);
        }
    }

    @Test
    public void testHashCode() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1),
                    Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
            assertEquals(goal1.hashCode(), goal2.hashCode());
            assertNotEquals(goal1.hashCode(), goal3.hashCode());
        }
    }

    @Test
    public void getGoalContextId() {
        Goal goal = new Goal(0, "test Goal", 0, false, null,
                true, null, false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertEquals(goal.getGoalContext(), GoalContext.getGoalContextById(1));
        Goal goal2 = new Goal(0, "test Goal", 0, false, null,
                false, null, false, GoalContext.getGoalContextById(4),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        assertEquals(goal2.getGoalContext(), GoalContext.getGoalContextById(4));
    }
}