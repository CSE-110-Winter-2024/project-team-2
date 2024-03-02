package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Tests for the methods of the Goal class.
 */
public class GoalTest {
    @Test
    public void getGoal() {
        for (int i = 0; i < 100; i++) {
            String theGoal = "Test goal " + i;
            Goal goal = new Goal(0, theGoal, 0, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            assertEquals(goal.getGoalText(), theGoal);
        }
    }

    @Test
    public void getId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(i, "Test Goal", 0, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            assertEquals(Integer.valueOf(i), goal.getId());
        }
    }

    @Test
    public void getSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", i, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            assertEquals(Integer.valueOf(i), goal.getSortOrder());
        }
    }

    @Test
    public void getIsComplete() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        assertFalse(goal.getIsComplete());
    }

    @Test
    public void getDateCompleted() {
        Calendar dateCompleted = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, dateCompleted, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        dateCompleted.set(Calendar.HOUR, 0);
        dateCompleted.set(Calendar.MINUTE, 0);
        dateCompleted.set(Calendar.SECOND, 0);
        dateCompleted.set(Calendar.MILLISECOND, 0);
        dateCompleted.set(Calendar.AM_PM, Calendar.AM);
        assertEquals(dateCompleted, goal.getDateCompleted());
    }

    @Test
    public void getIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        assertTrue(goal.getIsDisplayed());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, false,
                Calendar.getInstance(), false, false, Goal.RecurrencePattern.NONE);
        assertFalse(goal2.getIsDisplayed());
    }

    @Test
    public void setIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        assertTrue(goal.getIsDisplayed());

        goal.setIsDisplayed(false);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void changeIsCompleteStatus() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        goal.changeIsCompleteStatus();
        assertTrue(goal.isComplete);
    }

    @Test
    public void updateIsDisplayed() {
        Calendar currDate = Calendar.getInstance();
        Calendar dateCompleted = Calendar.getInstance();

        // When not goal is not completed
        Goal goal = new Goal(0, "test Goal", 0, false, null, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        goal.updateIsDisplayed(currDate);
        assertTrue(goal.getIsDisplayed());

        // Same day goal is crossed off
        Goal goal1 = new Goal(0, "test Goal", 0, true, dateCompleted, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        goal1.updateIsDisplayed(currDate);
        assertTrue(goal1.getIsDisplayed());

        // Day after goal is crossed off
        currDate.add(Calendar.DATE, 1);
        Goal goal2 = new Goal(0, "test Goal", 0, true, dateCompleted, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        goal2.updateIsDisplayed(currDate);
        assertFalse(goal2.getIsDisplayed());
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
        Goal goal = new Goal(0, "test Goal", 0, true, dateCompleted, true,
                null, false, false, Goal.RecurrencePattern.NONE);
        goal.updateIsDisplayed(currDate);
        assertTrue(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM
        currDate.setTime(sdf.parse("Feb 17 02:01:00 AM 2024"));
        goal.updateIsDisplayed(currDate);
        assertFalse(goal.getIsDisplayed());

        // Goal is marked as complete at 11:00 PM on 2/17
        dateCompleted.setTime(sdf.parse("Feb 17 11:00:00 PM 2024"));
        goal.setDateCompleted(dateCompleted);

        // Goal should still be displayed at 1:30 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 01:30:00 AM 2024"));
        goal.updateIsDisplayed(currDate);
        assertTrue(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 02:01:00 AM 2024"));
        goal.updateIsDisplayed(currDate);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void withSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal actual = goal.withSortOrder(i);
            assertEquals(Integer.valueOf(i), actual.getSortOrder());
        }
    }

    @Test
    public void withId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal actual = goal.withId(i);
            assertEquals(Integer.valueOf(i), actual.getId());
        }
    }

    @Test
    public void testEquals() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            assertEquals(goal1, goal2);
            assertNotEquals(goal1, goal3);
        }
    }

    @Test
    public void testHashCode() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true,
                    null, false, false, Goal.RecurrencePattern.NONE);
            assertEquals(goal1.hashCode(), goal2.hashCode());
            assertNotEquals(goal1.hashCode(), goal3.hashCode());
        }
    }
}