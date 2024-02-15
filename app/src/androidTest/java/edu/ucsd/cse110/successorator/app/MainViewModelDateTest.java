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
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;

/**
 * Tests the date-related functionality of MainViewModel; specifically, advancing the date one day
 * forward and observing changes to the current date.
 */
public class MainViewModelDateTest {
    private int observeCallsMade = 0;
    private Calendar lastObservedDate = null;
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
    public void advanceDateForward() {
        Calendar calendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 14);
        GoalRepository goalRepository = new RoomGoalRepository(goalsDao);
        DateRepository dateRepository = new DateRepository(new MockDateProvider(calendar));
        MainViewModel mainViewModel = new MainViewModel(goalRepository, dateRepository);

        assertEquals(mainViewModel.getDate().getValue(), calendar);
        mainViewModel.getDate().observe(newDate -> {
            this.observeCallsMade++;
            lastObservedDate = newDate;
        });
        assertEquals(lastObservedDate, calendar);
        // Our observer should have been called once when we added it
        assertEquals(observeCallsMade, 1);

        mainViewModel.advanceDateOneDayForward();
        Calendar advancedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 15);
        assertEquals(mainViewModel.getDate().getValue(), advancedCalendar);
        assertEquals(lastObservedDate, advancedCalendar);
        assertEquals(observeCallsMade, 2);

        mainViewModel.advanceDateOneDayForward();
        advancedCalendar = new GregorianCalendar(2024, Calendar.FEBRUARY, 16);
        assertEquals(mainViewModel.getDate().getValue(), advancedCalendar);
        assertEquals(lastObservedDate, advancedCalendar);
        assertEquals(observeCallsMade, 3);
    }
}
