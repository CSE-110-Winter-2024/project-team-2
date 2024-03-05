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

import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.date.CurrentDateProvider;

/**
 * Tests the selected goal context ID functionality of the MainViewModel.
 */
public class MainViewModelSelectedGoalContextTest {
    private int observeCallsMade = 0;
    private Integer lastObservedSelectedGoalContextId;
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
    public void selectedGoalContextId() {
        GoalRepository goalRepository = new RoomGoalRepository(goalsDao);
        DateRepository dateRepository = new DateRepository(new CurrentDateProvider());
        ViewRepository viewRepository = new ViewRepository();
        MainViewModel mainViewModel = new MainViewModel(goalRepository, dateRepository, viewRepository);

        // Selected goal context ID should initially be null
        assertNull(mainViewModel.getSelectedGoalContextId().getValue());

        mainViewModel.getSelectedGoalContextId().observe(newSelectedGoalContextId -> {
            this.observeCallsMade++;
            lastObservedSelectedGoalContextId = newSelectedGoalContextId;
        });
        assertNull(lastObservedSelectedGoalContextId);
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        mainViewModel.setSelectedGoalContextId(3);
        assertEquals(mainViewModel.getSelectedGoalContextId().getValue(), (Integer) 3);
        assertEquals(lastObservedSelectedGoalContextId, (Integer) 3);
        // Our observer should have been called again when setting the goal context ID
        assertEquals(observeCallsMade, 2);
    }
}
