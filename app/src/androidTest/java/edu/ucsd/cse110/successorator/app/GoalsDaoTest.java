package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * Tests the database-related methods of the GoalsDao class, i.e. CRUD operations on the goals DB
 * using a mock Room DB.
 */
public class GoalsDaoTest {
    private GoalsDao goalsDao;
    private SuccessoratorDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class).build();
        goalsDao = db.goalsDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertGoal() {
        GoalEntity goalEntity = new GoalEntity("goal1", 1, false, null, true);
        Long goal1Id = goalsDao.insert(goalEntity);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(1, allGoals.size());
        GoalEntity goalEntityFromDb = goalsDao.find(Math.toIntExact(goal1Id));
        assertEquals(goalEntity.goalText, goalEntityFromDb.goalText);
    }

    @Test
    public void insertMultipleGoals() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(2, allGoals.size());
    }

    @Test
    public void goalsCount() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false, null, true);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
    }

    @Test
    public void minSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false, null, true);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int minSortOrder = goalsDao.getMinSortOrder();
        assertEquals(2, minSortOrder);
    }

    @Test
    public void maxSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false, null, true);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int maxSortOrder = goalsDao.getMaxSortOrder();
        assertEquals(5, maxSortOrder);
    }

    @Test
    public void changeIsCompleteStatus() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);
        goalsDao.changeIsCompleteStatus(1);
        assertTrue(goalsDao.find(1).isComplete);
    }

    @Test
    public void changeIsDisplayedStatus() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);

        goalsDao.changeIsDisplayedStatus(1, true);
        assertTrue(goalsDao.find(1).isDisplayed);

        goalsDao.changeIsDisplayedStatus(1, false);
        assertFalse(goalsDao.find(1).isDisplayed);
    }

    @Test
    public void setDateCompleted() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);
        assertNull(goalsDao.find(1).dateCompleted);

        Calendar dateCompleted = Calendar.getInstance();
        goalsDao.changeIsCompleteStatus(1);
        goalsDao.setDateCompleted(1, dateCompleted);

        assertEquals(dateCompleted, goalsDao.find(1).dateCompleted);
    }

    @Test
    public void append() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        GoalEntity goalEntityToAppend = new GoalEntity("goal3", -100, false, null, true);
        int appendedGoalId = goalsDao.append(goalEntityToAppend);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
        GoalEntity appendedGoalFromDb = goalsDao.find(appendedGoalId);
        assertEquals(appendedGoalFromDb.goalText, goalEntityToAppend.goalText);
    }

    @Test
    public void findAll() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, true, null, true);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false, null, true);
        GoalEntity goalEntity4 = new GoalEntity("goal4", 4, true, null, true);
        GoalEntity goalEntity5 = new GoalEntity("goal5", 5, false, null, true);

        List<GoalEntity> goals = List.of(goalEntity1, goalEntity2, goalEntity3, goalEntity4, goalEntity5);
        goalsDao.insert(goals);
        List<GoalEntity> allGoals = goalsDao.findAll();
        List<GoalEntity> sortedGoals = List.of(goalEntity1, goalEntity3, goalEntity5, goalEntity2, goalEntity4);
        for (int i = 0; i < 5; i++) {
            assertEquals(sortedGoals.get(i).goalText, allGoals.get(i).goalText);
            assertEquals(sortedGoals.get(i).sortOrder, allGoals.get(i).sortOrder);
            assertEquals(sortedGoals.get(i).isComplete, allGoals.get(i).isComplete);
            assertEquals(sortedGoals.get(i).dateCompleted, allGoals.get(i).dateCompleted);
            assertEquals(sortedGoals.get(i).isDisplayed, allGoals.get(i).isDisplayed);
        }
    }

    @Test
    public void moveToTop() {
        Goal goal1 = new Goal(5, "goal1", 1, false, null, true);
        GoalEntity goalEntity1 = GoalEntity.fromGoal(goal1);
        Goal goal2 = new Goal(10, "goal2", 2, false, null, true);
        GoalEntity goalEntity2 = GoalEntity.fromGoal(goal2);
        Goal goal3 = new Goal(3, "goal3", 4, false, null, true);
        GoalEntity goalEntity3 = GoalEntity.fromGoal(goal3);
        List<GoalEntity> GoalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(GoalsToInsert);
        goalsDao.moveToTop(10);
        assertEquals(0, goalsDao.find(10).sortOrder);
        assertEquals(1, goalsDao.find(5).sortOrder);
        assertEquals(4, goalsDao.find(3).sortOrder);
    }
}
