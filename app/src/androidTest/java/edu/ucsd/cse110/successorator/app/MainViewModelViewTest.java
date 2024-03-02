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
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.CurrentViewProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * Tests the view-related functionality of MainViewModel; specifically, changing the
 * view and observing changes to the current view.
 */
public class MainViewModelViewTest {
    private int observeCallsMade = 0;
    private ViewOptions lastObservedView = null;
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
        ViewOptions defaultView = ViewOptions.TODAY;
        Calendar calendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 14);
        GoalRepository goalRepository = new RoomGoalRepository(goalsDao);
        DateRepository dateRepository = new DateRepository(new MockDateProvider(calendar));
        ViewRepository viewRepository = new ViewRepository(new CurrentViewProvider());
        MainViewModel mainViewModel = new MainViewModel(goalRepository, dateRepository, viewRepository);

        assertEquals(mainViewModel.getView().getValue(), defaultView);
        mainViewModel.getView().observe(viewType -> {
            this.observeCallsMade++;
            lastObservedView = viewType;
        });
        assertEquals(lastObservedView, defaultView);
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        mainViewModel.setView(ViewOptions.TOMORROW);
        ViewOptions tomorrowView = ViewOptions.TOMORROW;
        assertEquals(mainViewModel.getView().getValue(), tomorrowView);
        assertEquals(lastObservedView, tomorrowView);
        assertEquals(observeCallsMade, 2);

        mainViewModel.setView(ViewOptions.PENDING);
        ViewOptions pendingView = ViewOptions.PENDING;
        assertEquals(mainViewModel.getView().getValue(), pendingView);
        assertEquals(lastObservedView, pendingView);
        assertEquals(observeCallsMade, 3);

        mainViewModel.setView(ViewOptions.RECURRING);
        ViewOptions recurringView = ViewOptions.RECURRING;
        assertEquals(mainViewModel.getView().getValue(), recurringView);
        assertEquals(lastObservedView, recurringView);
        assertEquals(observeCallsMade, 4);
    }
}
