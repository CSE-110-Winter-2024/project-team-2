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

import javax.swing.text.View;

import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Tests for the methods of the Goal class.
 */
public class GoalTest {
    @Test
    public void getGoal() {
        for (int i = 0; i < 100; i++) {
            String theGoal = "Test goal " + i;
            Goal goal = new Goal(0, theGoal, 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            assertEquals(goal.getGoalText(), theGoal);
        }
    }

    @Test
    public void getId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(i, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            assertEquals(Integer.valueOf(i), goal.getId());
        }
    }

    @Test
    public void getSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            assertEquals(Integer.valueOf(i), goal.getSortOrder());
        }
    }

    @Test
    public void getIsComplete() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertFalse(goal.getIsComplete());
    }

    @Test
    public void getDateCompleted() {
        Calendar dateCompleted = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, dateCompleted, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        dateCompleted.set(Calendar.HOUR, 0);
        dateCompleted.set(Calendar.MINUTE, 0);
        dateCompleted.set(Calendar.SECOND, 0);
        dateCompleted.set(Calendar.MILLISECOND, 0);
        dateCompleted.set(Calendar.AM_PM, Calendar.AM);
        assertEquals(dateCompleted, goal.getDateCompleted());
    }

    @Test
    public void getIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertTrue(goal.getIsDisplayed());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, false, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertFalse(goal2.getIsDisplayed());
    }

    @Test
    public void setIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertTrue(goal.getIsDisplayed());

        goal.setIsDisplayed(false);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void getGoalDate() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertEquals(currDate, goal.getGoalDate());
    }

    @Test
    public void getIsPending() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertFalse(goal.getIsPending());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, true, currDate, true, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        assertTrue(goal2.getIsPending());
    }

    @Test
    public void setGoalDate() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal.setGoalDate(currDate);
        assertEquals(currDate, goal.goalDate);
    }

    @Test
    public void setIsPending() {
        Calendar currDate = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, currDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal.setIsPending(true);
        assertTrue(goal.isPending);
    }

    @Test
    public void changeIsCompleteStatus() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
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
        Goal goal = new Goal(0, "test Goal", 0, false, null, true, todayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal made yesterday
        goal = new Goal(0, "test Goal", 0, false, null, true, yesterdayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal made TOMORROW
        Goal goal1 = new Goal(0, "test Goal", 0, false, null, true, tommorowGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal1.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertFalse(goal1.getIsDisplayed());

        // When not goal is not completed, TODAY view, goal is PENDING
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, true, null, isPending, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal2.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertFalse(goal2.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made TOMORROW
        Goal goal3 = new Goal(0, "test Goal", 0, false, null, true, tommorowGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal3.updateIsDisplayed(tomorrowDate, ViewOptions.TOMORROW);
        assertTrue(goal3.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made TODAY
        goal3 = new Goal(0, "test Goal", 0, false, null, true, todayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal3.updateIsDisplayed(tomorrowDate, ViewOptions.TOMORROW);
        assertFalse(goal3.getIsDisplayed());

        // When not goal is not completed, TOMORROW view, goal made yesterday
        goal3 = new Goal(0, "test Goal", 0, false, null, true, yesterdayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal3.updateIsDisplayed(tomorrowDate, ViewOptions.TOMORROW);
        assertFalse(goal3.getIsDisplayed());

        // Today list: Same day goal is crossed off
        Goal goal4 = new Goal(0, "test Goal", 0, true, dateCompleted, true, todayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal4.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertTrue(goal4.getIsDisplayed());

        // Today list: Day after goal is crossed off
        currDate.add(Calendar.DATE, 1);
        Goal goal5 = new Goal(0, "test Goal", 0, true, dateCompleted, true, todayGoalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal5.updateIsDisplayed(currDate, ViewOptions.TODAY);
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
        Goal goal = new Goal(0, "test Goal", 0, true, dateCompleted, true, goalDate, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM
        currDate.setTime(sdf.parse("Feb 17 02:01:00 AM 2024"));
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertTrue(goal.getIsDisplayed());

        // Goal is marked as complete at 11:00 PM on 2/17
        dateCompleted.setTime(sdf.parse("Feb 17 11:00:00 PM 2024"));
        goal.setDateCompleted(dateCompleted);

        // Goal should still be displayed at 1:30 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 01:30:00 AM 2024"));
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertFalse(goal.getIsDisplayed());

        // Goal should not be displayed anymore at 2:01 AM on 2/18
        currDate.setTime(sdf.parse("Feb 18 02:01:00 AM 2024"));
        goal.updateIsDisplayed(currDate, ViewOptions.TODAY);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void withSortOrder() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal actual = goal.withSortOrder(i);
            assertEquals(Integer.valueOf(i), actual.getSortOrder());
        }
    }

    @Test
    public void withId() {
        for (int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal actual = goal.withId(i);
            assertEquals(Integer.valueOf(i), actual.getId());
        }
    }

    @Test
    public void testEquals() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            assertEquals(goal1, goal2);
            assertNotEquals(goal1, goal3);
        }
    }

    @Test
    public void testHashCode() {
        for (int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true, null, false, GoalContext.getGoalContextById(1), 0, Goal.RecurrencePattern.NONE, null, null);
            assertEquals(goal1.hashCode(), goal2.hashCode());
            assertNotEquals(goal1.hashCode(), goal3.hashCode());
        }
    }

    @Test
    public void getGoalContextId() {
        Goal goal = new Goal(0, "test Goal", 0, false, null,
                true, null, false, GoalContext.getGoalContextById(1),
                0, Goal.RecurrencePattern.NONE, null, null);
        assertEquals(goal.getGoalContext(), GoalContext.getGoalContextById(1));
        Goal goal2 = new Goal(0, "test Goal", 0, false, null,
                false, null, false, GoalContext.getGoalContextById(4),
                0, Goal.RecurrencePattern.NONE, null, null);
        assertEquals(goal2.getGoalContext(), GoalContext.getGoalContextById(4));
    }
}