package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * Tests the methods of the GoalEntity class
 */
public class GoalEntityTest {
    @Test
    public void goalEntityConstructor() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        assertEquals(goalEntity1.goalText, "goal1");
        assertEquals(goalEntity1.sortOrder, 1);
        assertFalse(goalEntity1.isComplete);
    }

    @Test
    public void fromGoal() {
        Goal goal1 = new Goal(5, "goal1", 1, false);
        GoalEntity goalEntity1 = GoalEntity.fromGoal(goal1);
        Goal goal2 = new Goal(10, "goal2", 2, true);
        GoalEntity goalEntity2 = GoalEntity.fromGoal(goal2);
        assertEquals(goalEntity1.id, (Integer) 5);
        assertEquals(goalEntity1.goalText, "goal1");
        assertEquals(goalEntity1.sortOrder, 1);
        assertFalse(goalEntity1.isComplete);
        assertEquals(goalEntity2.id, (Integer) 10);
        assertEquals(goalEntity2.goalText, "goal2");
        assertEquals(goalEntity2.sortOrder, 2);
        assertTrue(goalEntity2.isComplete);
    }

    @Test
    public void toGoal() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, true);
        Goal goal1 = goalEntity1.toGoal();
        Goal goal2 = goalEntity2.toGoal();
        assertEquals(goal1.goalText, "goal1");
        assertEquals(goal1.sortOrder, (Integer) 1);
        assertFalse(goal1.isComplete);
        assertEquals(goal2.goalText, "goal2");
        assertEquals(goal2.sortOrder, (Integer) 2);
        assertTrue(goal2.isComplete);
    }

}
