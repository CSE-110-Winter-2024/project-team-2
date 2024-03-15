package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.lib.domain.FocusModeRepository;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;

/**
 * Tests the focusContext-related functionality of MainViewModel; specifically, changing the
 * focus context and observing changes to the current context.
 */
public class MainViewModelFocusContextTest {
    private int observeCallsMade = 0;
    private GoalContext lastObservedFocusContext = null;
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
    public void changeViews() {
        GoalContext defaultView = null;
        Calendar calendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 14);
        GoalRepository goalRepository = new RoomGoalRepository(goalsDao);
        DateRepository dateRepository = new DateRepository(new MockDateProvider(calendar));
        ViewRepository viewRepository = new ViewRepository();
        FocusModeRepository focusRepository = new FocusModeRepository();
        MainViewModel mainViewModel = new MainViewModel(goalRepository, dateRepository, viewRepository, focusRepository);

        assertEquals(mainViewModel.getFocusContext().getValue(), defaultView);
        mainViewModel.getFocusContext().observe(context -> {
            this.observeCallsMade++;
            lastObservedFocusContext= context;
        });
        assertEquals(lastObservedFocusContext, defaultView);
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        mainViewModel.setFocusContext(GoalContext.defaultGoalContexts.get(0));
        GoalContext homeContext = GoalContext.defaultGoalContexts.get(0);
        assertEquals(mainViewModel.getFocusContext().getValue(), homeContext);
        assertEquals(lastObservedFocusContext, homeContext);
        assertEquals(observeCallsMade, 2);

        mainViewModel.setFocusContext(GoalContext.defaultGoalContexts.get(1));
        GoalContext workContext = GoalContext.defaultGoalContexts.get(1);
        assertEquals(mainViewModel.getFocusContext().getValue(), workContext);
        assertEquals(lastObservedFocusContext, workContext);
        assertEquals(observeCallsMade, 3);

        mainViewModel.setFocusContext(GoalContext.defaultGoalContexts.get(2));
        GoalContext schoolContext = GoalContext.defaultGoalContexts.get(2);
        assertEquals(mainViewModel.getFocusContext().getValue(), schoolContext);
        assertEquals(lastObservedFocusContext, schoolContext);
        assertEquals(observeCallsMade, 4);
        
        mainViewModel.setFocusContext(GoalContext.defaultGoalContexts.get(3));
        GoalContext errandsContext = GoalContext.defaultGoalContexts.get(3);
        assertEquals(mainViewModel.getFocusContext().getValue(), errandsContext);
        assertEquals(lastObservedFocusContext, errandsContext);
        assertEquals(observeCallsMade, 5);

        mainViewModel.setFocusContext(null);
        assertNull(mainViewModel.getFocusContext().getValue());
        assertNull(lastObservedFocusContext);
        assertEquals(observeCallsMade, 6);
    }
}
