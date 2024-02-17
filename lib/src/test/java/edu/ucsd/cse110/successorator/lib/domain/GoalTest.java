package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;

/**
 * Tests for the methods of the Goal class.
 */
public class GoalTest {
    @Test
    public void getGoal() {
        for(int i = 0; i < 100; i++) {
            String theGoal = "Test goal " + i;
            Goal goal = new Goal(0, theGoal, 0, false, null, true);
            assertEquals(goal.getGoalText(), theGoal);
        }
    }

    @Test
    public void getId() {
        for(int i = 0; i < 100; i++) {
            Goal goal = new Goal(i, "Test Goal", 0, false, null, true);
            assertEquals(Integer.valueOf(i), goal.getId());
        }
    }

    @Test
    public void getSortOrder() {
        for(int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", i, false, null, true);
            assertEquals(Integer.valueOf(i), goal.getSortOrder());
        }
    }

    @Test
    public void getIsComplete() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true);
        assertFalse(goal.getIsComplete());
    }

    @Test
    public void getDateCompleted() {
        Calendar dateCompleted = Calendar.getInstance();
        Goal goal = new Goal(0, "test Goal", 0, false, dateCompleted, true);
        dateCompleted.set(Calendar.HOUR, 0);
        dateCompleted.set(Calendar.MINUTE, 0);
        dateCompleted.set(Calendar.SECOND, 0);
        dateCompleted.set(Calendar.MILLISECOND, 0);
        dateCompleted.set(Calendar.AM_PM, Calendar.AM);
        assertEquals(dateCompleted, goal.getDateCompleted());
    }

    @Test
    public void getIsDisplayed() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true);
        assertTrue(goal.getIsDisplayed());
        Goal goal2 = new Goal(0, "test Goal", 0, false, null, false);
        assertFalse(goal.getIsDisplayed());
    }

    @Test
    public void changeIsCompleteStatus() {
        Goal goal = new Goal(0, "test Goal", 0, false, null, true);
        goal.changeIsCompleteStatus();
        assertTrue(goal.isComplete);
    }

    @Test
    public void updateIsDisplayed() {
        Calendar currDate = Calendar.getInstance();
        Calendar dateCompleted = Calendar.getInstance();

        // When not goal is not completed
        Goal goal = new Goal(0, "test Goal", 0, false, null, true);
        goal.updateIsDisplayed(currDate);
        assertTrue(goal.getIsDisplayed());

        // Same day goal is crossed off
        Goal goal1 = new Goal(0, "test Goal", 0, true, dateCompleted, true);
        goal1.updateIsDisplayed(currDate);
        assertTrue(goal1.getIsDisplayed());

        // Day after goal is crossed off
        currDate.add(Calendar.DATE, 1);
        Goal goal2 = new Goal(0, "test Goal", 0, true, dateCompleted, true);
        goal2.updateIsDisplayed(currDate);
        assertFalse(goal2.getIsDisplayed());
    }

    @Test
    public void withSortOrder() {
        for(int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true);
            Goal actual = goal.withSortOrder(i);
            assertEquals(Integer.valueOf(i), actual.getSortOrder());
        }
    }

    @Test
    public void withId() {
        for(int i = 0; i < 100; i++) {
            Goal goal = new Goal(0, "Test Goal", 0, false, null, true);
            Goal actual = goal.withId(i);
            assertEquals(Integer.valueOf(i), actual.getId());
        }
    }

    @Test
    public void testEquals() {
        for(int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true);
            assertEquals(goal1, goal2);
            assertNotEquals(goal1, goal3);
        }
    }

    @Test
    public void testHashCode() {
        for(int i = 0; i < 100; i++) {
            Goal goal1 = new Goal(i, "Test Goal", i+5, false, null, true);
            Goal goal2 = new Goal(i, "Test Goal", i+5, false, null, true);
            Goal goal3 = new Goal(i, "Test Goal", i, false, null, true);
            assertEquals(goal1.hashCode(), goal2.hashCode());
            assertNotEquals(goal1.hashCode(), goal3.hashCode());
        }
    }
}