package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the methods of the Goal class.
 */
public class GoalTest {
    @Test
    public void getGoal() {
        for( int i = 0; i < 100; i++){
            String theGoal = "Test goal " + i;
            Goal goal = new Goal(0, theGoal, 0);
            assertEquals(goal.getGoalText(), theGoal);
        }
    }

    @Test
    public void getId() {
        for( int i = 0; i < 100; i++){
            Goal goal = new Goal(i, "Test Goal", 0);
            assertEquals(Integer.valueOf(i), goal.getId());
        }
    }

    @Test
    public void getSortOrder() {
        for( int i = 0; i < 100; i++){
            Goal goal = new Goal(0, "Test Goal", i);
            assertEquals(Integer.valueOf(i), goal.getSortOrder());
        }
    }

    @Test
    public void withSortOrder() {
        for( int i = 0; i < 100; i++){
            Goal goal = new Goal(0, "Test Goal", 0);
            Goal actual = goal.withSortOrder(i);
            assertEquals(Integer.valueOf(i), actual.getSortOrder());
        }
    }

    @Test
    public void withId() {
        for( int i = 0; i < 100; i++){
            Goal goal = new Goal(0, "Test Goal", 0);
            Goal actual = goal.withId(i);
            assertEquals(Integer.valueOf(i), actual.getId());
        }
    }

    @Test
    public void testEquals() {
        for( int i = 0; i < 100; i++){
            Goal goal1 = new Goal(i, "Test Goal", i+5);
            Goal goal2 = new Goal(i, "Test Goal", i+5);
            Goal goal3 = new Goal(i, "Test Goal", i);
            assertTrue(goal1.equals(goal2));
            assertFalse(goal1.equals(goal3));
        }
    }

    @Test
    public void testHashCode() {
        for( int i = 0; i < 100; i++){
            Goal goal1 = new Goal(i, "Test Goal", i+5);
            Goal goal2 = new Goal(i, "Test Goal", i+5);
            Goal goal3 = new Goal(i, "Test Goal", i);
            assertEquals(goal1.hashCode(), goal2.hashCode());
            assertNotEquals(goal1.hashCode(), goal3.hashCode());
        }
    }
}