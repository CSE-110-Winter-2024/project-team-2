package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.date.CurrentDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.CurrentViewProvider;

/**
 * Tests the goals-related functionality of MainViewModel; specifically, appending goals and
 * observing changes to the ordered list of goals.
 */
public class MainViewModelGoalTest {
    private int observeCallsMade = 0;
    private List<Goal> lastObservedGoals = null;
    private GoalsDao goalsDao;
    private SuccessoratorDatabase db;

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
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void getGoalsAndAppend() {
        GoalRepository goalRepository = new RoomGoalRepository(goalsDao);
        DateRepository dateRepository = new DateRepository(new CurrentDateProvider());
        ViewRepository viewRepository = new ViewRepository(new CurrentViewProvider());
        MainViewModel mainViewModel = new MainViewModel(goalRepository, dateRepository, viewRepository);

        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 0);
        mainViewModel.getOrderedGoals().observe(newGoals -> {
            this.observeCallsMade++;
            lastObservedGoals = newGoals;
        });
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        Goal goal1 = new Goal(1, "Goal 1", 1, false, null, true);
        mainViewModel.append(goal1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(0), goal1);
        assertEquals(lastObservedGoals.size(), 1);
        assertEquals(lastObservedGoals.get(0), goal1);
        assertEquals(observeCallsMade, 2);

        Goal goal2 = new Goal(2, "Goal 2", 2, false, null, true);
        mainViewModel.append(goal2);
        assertEquals(mainViewModel.getOrderedGoals().getValue().size(), 2);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(0), goal1);
        assertEquals(mainViewModel.getOrderedGoals().getValue().get(1), goal2);
        assertEquals(lastObservedGoals.size(), 2);
        assertEquals(lastObservedGoals.get(0), goal1);
        assertEquals(lastObservedGoals.get(1), goal2);
        assertEquals(observeCallsMade, 3);
    }
}
