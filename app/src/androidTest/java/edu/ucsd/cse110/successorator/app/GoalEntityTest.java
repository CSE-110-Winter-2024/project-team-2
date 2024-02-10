package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class GoalEntityTest {
    @Test
    public void goalEntityConstructor() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        assertEquals(goalEntity1.goalText, "goal1");
        assertEquals(goalEntity1.sortOrder, 1);
    }

    @Test
    public void fromGoal() {
        Goal goal1 = new Goal(5, "goal1", 1, false);
        GoalEntity goalEntity1 = GoalEntity.fromGoal(goal1);
        Goal goal2 = new Goal(10, "goal2", 2, false);
        GoalEntity goalEntity2 = GoalEntity.fromGoal(goal2);
        assertEquals(goalEntity1.id, (Integer) 5);
        assertEquals(goalEntity1.goalText, "goal1");
        assertEquals(goalEntity1.sortOrder, 1);
        assertEquals(goalEntity2.id, (Integer) 10);
        assertEquals(goalEntity2.goalText, "goal2");
        assertEquals(goalEntity2.sortOrder, 2);
    }

    @Test
    public void toGoal() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        Goal goal1 = goalEntity1.toGoal();
        Goal goal2 = goalEntity2.toGoal();
        assertEquals(goal1.goalText, "goal1");
        assertEquals(goal1.sortOrder, (Integer) 1);
        assertEquals(goal2.goalText, "goal2");
        assertEquals(goal2.sortOrder, (Integer) 2);
    }
}
