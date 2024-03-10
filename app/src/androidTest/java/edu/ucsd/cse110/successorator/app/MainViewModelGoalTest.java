package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;
import edu.ucsd.cse110.successorator.lib.domain.GoalFactory;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.date.CurrentDateProvider;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Tests the goals-related functionality of MainViewModel; specifically, appending goals and
 * observing changes to the ordered list of goals.
 */
public class MainViewModelGoalTest {
    private int observeCallsMade = 0;
    private List<Goal> lastObservedGoals = null;
    private GoalsDao goalsDao;
    private SuccessoratorDatabase db;
    private GoalRepository goalRepository;
    private MainViewModel mainViewModel;

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
        DateRepository dateRepository = new DateRepository(new CurrentDateProvider());
        ViewRepository viewRepository = new ViewRepository();
        mainViewModel = new MainViewModel(goalRepository, dateRepository, viewRepository);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void getGoalsAndAppend() {
        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 0);
        mainViewModel.getOrderedGoals().observe(newGoals -> {
            this.observeCallsMade++;
            lastObservedGoals = newGoals;
        });
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        Goal goal1 = new Goal(1, "Goal 1", 1, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        mainViewModel.append(goal1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(0), goal1);
        assertEquals(lastObservedGoals.size(), 1);
        assertEquals(lastObservedGoals.get(0), goal1);
        assertEquals(observeCallsMade, 2);

        Goal goal2 = new Goal(2, "Goal 2", 2, false, null, true, Calendar.getInstance(), false, GoalContext.getGoalContextById(1), Goal.RecurType.NOT_RECURRING, Goal.RecurrencePattern.NONE, null, null, null);
        mainViewModel.append(goal2);
        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 2);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(0), goal1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(1), goal2);
        assertEquals(lastObservedGoals.size(), 2);
        assertEquals(lastObservedGoals.get(0), goal1);
        assertEquals(lastObservedGoals.get(1), goal2);
        assertEquals(observeCallsMade, 3);
    }

    @Test
    public void changeIsCompleteStatus() {
        Calendar goalDate = mainViewModel.getDate().getValue();

        // Mark today goal as complete
        Goal goal = new GoalFactory().makeOneTimeGoal("Goal 1", new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.TODAY),
                ViewOptions.TODAY, 1);
        int goalId = mainViewModel.append(goal);
        mainViewModel.changeIsCompleteStatus(goalId);
        Goal dbGoal = goalRepository.rawFind(goalId);
        assertTrue(dbGoal.getIsComplete());
        assertEquals(dbGoal.getDateCompleted(), mainViewModel.getDate().getValue());

        mainViewModel.changeIsCompleteStatus(goalId);
        assertFalse(mainViewModel.getOrderedGoals().getValue().get(0).getIsComplete());

        // Mark tomorrow goal as complete
        goal = new GoalFactory().makeOneTimeGoal("Goal 1", new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.TOMORROW), ViewOptions.TOMORROW, 1);
        goalId = mainViewModel.append(goal);
        mainViewModel.changeIsCompleteStatus(goalId);
        dbGoal = goalRepository.rawFind(goalId);
        assertTrue(dbGoal.getIsComplete());
        assertEquals(dbGoal.getDateCompleted(), mainViewModel.getDate().getValue());

        mainViewModel.changeIsCompleteStatus(goalId);
        dbGoal = goalRepository.rawFind(goalId);
        assertFalse(dbGoal.getIsComplete());

        // Mark pending goal as complete
        goal = new GoalFactory().makeOneTimeGoal("Goal 1", new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.PENDING),
                ViewOptions.PENDING, 1);
        goalId = mainViewModel.append(goal);
        dbGoal = goalRepository.rawFind(goalId);
        assertTrue(dbGoal.getIsPending());
        mainViewModel.changeIsCompleteStatus(goalId);

        dbGoal = goalRepository.rawFind(goalId);
        assertFalse(dbGoal.getIsPending());
        assertTrue(dbGoal.getIsComplete());
        assertEquals(dbGoal.getDateCompleted(), mainViewModel.getDate().getValue());

        mainViewModel.changeIsCompleteStatus(goalId);
        dbGoal = goalRepository.rawFind(goalId);
        assertFalse(dbGoal.getIsComplete());
        assertFalse(dbGoal.getIsPending());
    }

    @Test
    public void moveToToday() {
        Calendar goalDate = mainViewModel.getDate().getValue();

        Goal goal = new GoalFactory().makeOneTimeGoal("Goal 1", new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.PENDING),
                ViewOptions.PENDING, 1);
        int goalId = mainViewModel.append(goal);
        Goal dbGoal = goalRepository.rawFind(goalId);
        assertTrue(dbGoal.getIsPending());
        mainViewModel.moveToToday(goalId);

        dbGoal = goalRepository.rawFind(goalId);
        assertFalse(dbGoal.getIsPending());
        assertFalse(dbGoal.getIsComplete());
        assertEquals(dbGoal.getGoalDate(), new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.TODAY));
    }

    @Test
    public void moveToTomorrow() {
        Calendar goalDate = mainViewModel.getDate().getValue();

        Goal goal = new GoalFactory().makeOneTimeGoal("Goal 1", new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.PENDING),
                ViewOptions.PENDING, 1);
        int goalId = mainViewModel.append(goal);
        Goal dbGoal = goalRepository.rawFind(goalId);
        assertTrue(dbGoal.getIsPending());
        mainViewModel.moveToTomorrow(goalId);

        dbGoal = goalRepository.rawFind(goalId);
        assertFalse(dbGoal.getIsPending());
        assertFalse(dbGoal.getIsComplete());
        assertEquals(dbGoal.getGoalDate(), new MockDateProvider(goalDate).getCurrentViewDate(ViewOptions.TOMORROW));
    }
}
