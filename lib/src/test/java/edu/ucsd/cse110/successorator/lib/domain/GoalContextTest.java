package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the methods of the GoalContext class.
 */
public class GoalContextTest {
    @Test
    public void getGoalContextById() {
        GoalContext goalContext = GoalContext.getGoalContextById(1);
        assertEquals(goalContext.getName(), "Home");
        assertEquals(goalContext.getId(), (Integer) 1);
    }
    @Test
    public void getFirstLetterOfName() {
        GoalContext goalContext = GoalContext.getGoalContextById(1);
        assertEquals(goalContext.getFirstLetterOfName(), 'H');
    }

    @Test
    public void equals() {
        // Contexts with the same field values should be equal,
        GoalContext goalContext1 = new GoalContext("Test", 5, "#FFFFFF");
        GoalContext goalContext2 = new GoalContext("Test", 5, "#FFFFFF");
        assertEquals(goalContext1, goalContext2);

        // Contexts with different field values should not be equal.
        GoalContext goalContext3 = new GoalContext("Test2", 5, "#FFFFFF");
        assertNotEquals(goalContext1, goalContext3);
    }
}
