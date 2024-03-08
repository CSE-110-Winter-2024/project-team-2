package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;

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
        GoalEntity goalEntity = new GoalEntity("goal1", 1, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Long goal1Id = goalsDao.insert(goalEntity);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(1, allGoals.size());
        GoalEntity goalEntityFromDb = goalsDao.find(Math.toIntExact(goal1Id));
        assertEquals(goalEntity.goalText, goalEntityFromDb.goalText);
        assertEquals(goalEntity.contextId, goalEntityFromDb.contextId);
    }

    @Test
    public void insertMultipleGoals() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(2, allGoals.size());
    }

    @Test
    public void goalsCount() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
    }

    @Test
    public void minSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int minSortOrder = goalsDao.getMinSortOrder();
        assertEquals(2, minSortOrder);
    }

    @Test
    public void maxSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int maxSortOrder = goalsDao.getMaxSortOrder();
        assertEquals(5, maxSortOrder);
    }

    @Test
    public void changeIsCompleteStatus() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);
        goalsDao.changeIsCompleteStatus(1);
        assertTrue(goalsDao.find(1).isComplete);
    }

    @Test
    public void changeIsDisplayedStatus() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);

        goalsDao.changeIsDisplayedStatus(1, true);
        assertTrue(goalsDao.find(1).isDisplayed);

        goalsDao.changeIsDisplayedStatus(1, false);
        assertFalse(goalsDao.find(1).isDisplayed);
    }

    @Test
    public void setDateCompleted() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
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
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        GoalEntity goalEntityToAppend = new GoalEntity("goal3", -100, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        int appendedGoalId = goalsDao.append(goalEntityToAppend);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
        GoalEntity appendedGoalFromDb = goalsDao.find(appendedGoalId);
        assertEquals(appendedGoalFromDb.goalText, goalEntityToAppend.goalText);
        assertEquals(appendedGoalFromDb.contextId, goalEntityToAppend.contextId);
    }

    @Test
    public void findAll() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, true, null, true,
                Calendar.getInstance(), false, 2, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false, null, true,
                Calendar.getInstance(), false, 4, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity4 = new GoalEntity("goal4", 4, true, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity5 = new GoalEntity("goal5", 5, false, null, true,
                Calendar.getInstance(), false, 3, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

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
    public void getAllGoals() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, true, null, true,
                Calendar.getInstance(), false, 2, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false, null, true,
                Calendar.getInstance(), false, 4, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity4 = new GoalEntity("goal4", 4, true, null, true,
                Calendar.getInstance(), false, 1, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity5 = new GoalEntity("goal5", 5, false, null, true,
                Calendar.getInstance(), false, 3, Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

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
            assertEquals(sortedGoals.get(i).contextId, allGoals.get(i).contextId);
        }
    }

    @Test
    public void moveToTop() {
        Goal goal1 = new Goal(5, "goal1", 1, false, null,  true,
                Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity1 = GoalEntity.fromGoal(goal1);
        Goal goal2 = new Goal(10, "goal2", 2, false, null, true,
                Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity2 = GoalEntity.fromGoal(goal2);
        Goal goal3 = new Goal(3, "goal3", 4, false, null, true,
                Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity3 = GoalEntity.fromGoal(goal3);
        List<GoalEntity> GoalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(GoalsToInsert);
        goalsDao.moveToTop(10);
        assertEquals(0, goalsDao.find(10).sortOrder);
        assertEquals(1, goalsDao.find(5).sortOrder);
        assertEquals(4, goalsDao.find(3).sortOrder);
    }

    @Test
    public void getIsPendingStatus() {
        Goal goal = new Goal(0, "goal1", 1, false, null,  true,
                Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity = GoalEntity.fromGoal(goal);
        goalsDao.append(goalEntity);
        assertFalse(goalsDao.getIsPendingStatus(0));
    }

    @Test
    public void changeIsPendingStatus() {
        Goal goal = new Goal(3, "goal1", 1, false, null,  true, null,
                true, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity = GoalEntity.fromGoal(goal);
        goalsDao.append(goalEntity);
        goalsDao.changeIsPendingStatus(3, false);
        assertFalse(goalsDao.getIsPendingStatus(3));
    }

    @Test
    public void setGoalDate(){
        Goal goal = new Goal(3, "goal1", 1, false, null,  true, null,
                true, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        GoalEntity goalEntity = GoalEntity.fromGoal(goal);
        goalsDao.insert(goalEntity);
        assertNull(goalsDao.find(3).goalDate);
        Calendar goalDate = Calendar.getInstance();

        goalsDao.setGoalDate(3, goalDate);
        assertEquals(goalsDao.find(3).goalDate, goalDate);
    }

    @Test
    public void setNextRecurrence(){
        Calendar currDate = Calendar.getInstance();
        Calendar nextRecurrence = (Calendar) currDate.clone();
        nextRecurrence.add(Calendar.DATE, 1);

        Goal goal = new Goal(3, "goal1", 1, false, null,  true, currDate,
                true, GoalContext.getGoalContextById(1), Goal.RecurType.RECURRING_INSTANCE, Goal.RecurrencePattern.DAILY, null, null, null);
        GoalEntity goalEntity = GoalEntity.fromGoal(goal);
        goalsDao.insert(goalEntity);
        assertNull(goalsDao.find(3).nextRecurrence);

        goalsDao.setNextRecurrence(3, nextRecurrence);
        assertNotNull(goalsDao.find(3).nextRecurrence);
        assertEquals(goalsDao.find(3).nextRecurrence, nextRecurrence);
        assertNotEquals(goalsDao.find(3).goalDate, goalsDao.find(3).nextRecurrence);
    }

}
