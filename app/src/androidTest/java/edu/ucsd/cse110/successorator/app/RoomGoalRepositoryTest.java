package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;


/**
 * Tests the RoomGoalRepository by mocking a Room database and ensuring our app's CRUD operations
 * work correctly.
 */
public class RoomGoalRepositoryTest {
    private GoalsDao goalsDao;
    private GoalRepository goalRepository;
    private SuccessoratorDatabase db;
    private int notifiedCount;

    /**
     * This rule allows us to call the Room LiveData observeForever method in our tests. Without
     * this rule, we get the error "Cannot invoke observeForever on a background thread"
     */
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class).build();
        goalsDao = db.goalsDao();
        goalRepository = new RoomGoalRepository(goalsDao);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void observeAppendGoal() {
        Subject<List<Goal>> allGoalsSubject = goalRepository.findAll();
        notifiedCount = 0;
        allGoalsSubject.observe(goals -> {
            notifiedCount++;
        });
        goalRepository.append(new Goal(null, "goal1", 1, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null));
        // Our observer should have been called once upon creation, and again upon appending a goal.
        assertEquals(2, notifiedCount);
        assertEquals(1, allGoalsSubject.getValue().size());
    }

    @Test
    public void observeUpdateGoal() {
        Goal goal = new Goal(1, "goal1", 1, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(1),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        goalRepository.save(goal);
        Subject<List<Goal>> allGoalsSubject = goalRepository.findAll();
        Subject<Goal> goalSubject = goalRepository.find(goal.id);
        notifiedCount = 0;
        allGoalsSubject.observe(goals -> {
            notifiedCount++;
        });
        goalSubject.observe(goals -> {
            notifiedCount++;
        });
        goalRepository.save(new Goal(1, "goalTextChanged", 2, false, null, true, Calendar.getInstance(), false,  GoalContext.getGoalContextById(3),
                Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null));

        // Each observer should have been called once upon creation, and again upon saving the goal.
        assertEquals(4, notifiedCount);
        assertEquals(1, allGoalsSubject.getValue().size());

        // Check that fields were updated on both subjects
        assertEquals("goalTextChanged", allGoalsSubject.getValue().get(0).getGoalText());
        assertEquals((Integer) 2, allGoalsSubject.getValue().get(0).getSortOrder());
        assertEquals((Integer) 3, allGoalsSubject.getValue().get(0).getGoalContext().getId());
        assertEquals("goalTextChanged", goalSubject.getValue().getGoalText());
        assertEquals((Integer) 2, goalSubject.getValue().getSortOrder());
        assertEquals((Integer) 3, goalSubject.getValue().getGoalContext().getId());
    }

    @Test
    public void observeSaveMultipleGoals() {
        Subject<List<Goal>> allGoalsSubject = goalRepository.findAll();
        notifiedCount = 0;
        allGoalsSubject.observe(goals -> {
            notifiedCount++;
        });
        List<Goal> goalsToSave = List.of(
                new Goal(null, "goal1", 1, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(2),
                        Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null),
                new Goal(null, "goal2", 2, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(4),
                        Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null)
        );
        goalRepository.save(goalsToSave);

        // Our observer should have been called once upon creation, and again upon saving the goals.
        assertEquals(2, notifiedCount);
        assertEquals(2, allGoalsSubject.getValue().size());

        // Ensure that the save() call propagated to the database
        assertEquals(2, goalsDao.count());
    }

    @Test
    public void sortByContextAsLiveData() {
        Calendar currDate = new MockDateProvider(Calendar.getInstance()).getCurrentViewDate(ViewOptions.TODAY);

        //Goals with different attributes in different creation orders to fully test sorting method
        Goal goal1 = new Goal(1, "Goal 1", 1, false, null, true, currDate, false, GoalContext.getGoalContextById(4), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal2 = new Goal(2, "Goal 2", 2, true, null, true, currDate, false, GoalContext.getGoalContextById(2), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal3 = new Goal(3, "Goal 3", 3, true, null, true, currDate, false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal4 = new Goal(4, "Goal 4", 4, false, null, true, currDate, false, GoalContext.getGoalContextById(3), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal5 = new Goal(5, "Goal 5", 5, true, null, true, currDate, false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal6 = new Goal(6, "Goal 6", 6, true, null, true, currDate, false, GoalContext.getGoalContextById(2), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal7 = new Goal(7, "Goal 7", 7, false, null, true, currDate, false, GoalContext.getGoalContextById(2), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        Goal goal8 = new Goal(8, "Goal 8", 8, false, null, true, currDate, false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);

        GoalEntity goalEntity1 = GoalEntity.fromGoal(goal1);
        GoalEntity goalEntity2 = GoalEntity.fromGoal(goal2);
        GoalEntity goalEntity3 = GoalEntity.fromGoal(goal3);
        GoalEntity goalEntity4 = GoalEntity.fromGoal(goal4);
        GoalEntity goalEntity5 = GoalEntity.fromGoal(goal5);
        GoalEntity goalEntity6 = GoalEntity.fromGoal(goal6);
        GoalEntity goalEntity7 = GoalEntity.fromGoal(goal7);
        GoalEntity goalEntity8 = GoalEntity.fromGoal(goal8);

        goalsDao.insert(goalEntity1);
        goalsDao.insert(goalEntity2);
        goalsDao.insert(goalEntity3);
        goalsDao.insert(goalEntity4);
        goalsDao.insert(goalEntity5);
        goalsDao.insert(goalEntity6);
        goalsDao.insert(goalEntity7);
        goalsDao.insert(goalEntity8);

        //Sort by completion -> contextId-> sort_order , if Completed: sort by dateCompleted
        LiveData<List<GoalEntity>> sortedGoalsLiveData = goalsDao.sortByContextAsLiveData();

        // Verify the results
        sortedGoalsLiveData.observeForever(sortedGoals -> {
            assertNotNull(sortedGoals);
            assertEquals(8, sortedGoals.size());
            assertEquals(goalEntity8.id, sortedGoals.get(0).id); //Active goal, context 1
            assertEquals(goalEntity7.id, sortedGoals.get(1).id); //Active goal, context 2
            assertEquals(goalEntity4.id, sortedGoals.get(2).id); //Active goal, context 3
            assertEquals(goalEntity1.id, sortedGoals.get(3).id); //Active goal, context 4
            assertEquals(goalEntity2.id, sortedGoals.get(4).id); //Completed goal -> sortOrder 2
            assertEquals(goalEntity3.id, sortedGoals.get(5).id); //Completed goal -> sortOrder 3
            assertEquals(goalEntity5.id, sortedGoals.get(6).id); //Completed goal -> sortOrder 5
            assertEquals(goalEntity6.id, sortedGoals.get(7).id); //Completed goal -> sortOrder 6
        });
    }

    @Test
    public void sortByContextAsLiveDataRecurring() {
        Calendar currDate = new MockDateProvider(Calendar.getInstance()).getCurrentViewDate(ViewOptions.TODAY);

        // Goal 1 is 2 days in the future
        currDate = (Calendar) currDate.clone();
        currDate.add(Calendar.DATE, 2);
        long goal1Id = goalsDao.insert(GoalEntity.fromGoal(
                new Goal(null, "Goal 1", 100, false, null, true, currDate, false, GoalContext.getGoalContextById(2), Goal.RecurType.RECURRING_TEMPLATE, Goal.RecurrencePattern.DAILY, null, null, null)
        ));

        // Goal 2 is 1 day in the future
        currDate = (Calendar) currDate.clone();
        currDate.add(Calendar.DATE, -1);
        long goal2Id = goalsDao.insert(GoalEntity.fromGoal(
            new Goal(null, "Goal 2", 1, false, null, true, currDate, false, GoalContext.getGoalContextById(4), Goal.RecurType.RECURRING_TEMPLATE, Goal.RecurrencePattern.DAILY, null, null, null)
        ));

        // Goal 3 is today
        currDate = (Calendar) currDate.clone();
        currDate.add(Calendar.DATE, -1);
        long goal3Id = goalsDao.insert(GoalEntity.fromGoal(
                new Goal(null, "Goal 3", 150, false, null, true, currDate, false, GoalContext.getGoalContextById(3), Goal.RecurType.RECURRING_TEMPLATE, Goal.RecurrencePattern.DAILY, null, null, null)
        ));

        // Goal 4 is 3 days in the future
        currDate = (Calendar) currDate.clone();
        currDate.add(Calendar.DATE, 3);
        long goal4Id = goalsDao.insert(GoalEntity.fromGoal(
                new Goal(null, "Goal 4", 2, false, null, true, currDate, false, GoalContext.getGoalContextById(1), Goal.RecurType.RECURRING_TEMPLATE, Goal.RecurrencePattern.DAILY, null, null, null)
        ));

        // For recurring templates, sort by goalDate, regardless of sort_order
        LiveData<List<GoalEntity>> sortedGoalsLiveData = goalsDao.sortByContextAsLiveData();

        // Verify the results
        sortedGoalsLiveData.observeForever(sortedGoals -> {
            assertNotNull(sortedGoals);
            assertEquals(4, sortedGoals.size());

            assertEquals((Integer) (int) goal3Id, sortedGoals.get(0).id); // Goal 3 is today
            assertEquals((Integer) (int) goal2Id, sortedGoals.get(1).id); // Goal 2 is 1 day in the future
            assertEquals((Integer) (int) goal1Id, sortedGoals.get(2).id); // Goal 1 is 2 days in the future
            assertEquals((Integer) (int) goal4Id, sortedGoals.get(3).id); // Goal 4 is 3 days in the future
        });
    }
}
