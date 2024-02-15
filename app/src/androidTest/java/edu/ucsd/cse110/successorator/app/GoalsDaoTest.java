package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;

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
        GoalEntity goalEntity = new GoalEntity("goal1", 1, false);
        Long goal1Id = goalsDao.insert(goalEntity);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(1, allGoals.size());
        GoalEntity goalEntityFromDb = goalsDao.find(Math.toIntExact(goal1Id));
        assertEquals(goalEntity.goalText, goalEntityFromDb.goalText);
    }

    @Test
    public void insertMultipleGoals() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        List<GoalEntity> allGoals = goalsDao.findAll();
        assertEquals(2, allGoals.size());
    }

    @Test
    public void goalsCount() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 1, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 3, false);
        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
    }

    @Test
    public void minSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false);
        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int minSortOrder = goalsDao.getMinSortOrder();
        assertEquals(2, minSortOrder);
    }

    @Test
    public void maxSortOrder() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        GoalEntity goalEntity3 = new GoalEntity("goal3", 4, false);
        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2, goalEntity3);
        goalsDao.insert(goalsToInsert);
        int maxSortOrder = goalsDao.getMaxSortOrder();
        assertEquals(5, maxSortOrder);
    }

    @Test
    public void changeIsCompleteStatus() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false);
        goalEntity1.id = 1;
        goalsDao.insert(goalEntity1);
        goalsDao.changeIsCompleteStatus(1);
        assertTrue(goalsDao.find(1).isComplete);
    }

    @Test
    public void append() {
        GoalEntity goalEntity1 = new GoalEntity("goal1", 5, false);
        GoalEntity goalEntity2 = new GoalEntity("goal2", 2, false);
        List<GoalEntity> goalsToInsert = List.of(goalEntity1, goalEntity2);
        goalsDao.insert(goalsToInsert);
        GoalEntity goalEntityToAppend = new GoalEntity("goal3", -100, false);
        int appendedGoalId = goalsDao.append(goalEntityToAppend);
        int goalsCount = goalsDao.count();
        assertEquals(3, goalsCount);
        GoalEntity appendedGoalFromDb = goalsDao.find(appendedGoalId);
        assertEquals(appendedGoalFromDb.goalText, goalEntityToAppend.goalText);
    }

}
