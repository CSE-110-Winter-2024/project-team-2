package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import kotlinx.coroutines.Dispatchers;

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
        goalRepository.append(new Goal(null, "goal1", 1));
        // Our observer should have been called once upon creation, and again upon appending a goal.
        assertEquals(2, notifiedCount);
        assertEquals(1, allGoalsSubject.getValue().size());
    }

    @Test
    public void observeUpdateGoal() {
        Goal goal = new Goal(1, "goal1", 1);
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
        goalRepository.save(new Goal(1, "goalTextChanged", 2));

        // Each observer should have been called once upon creation, and again upon saving the goal.
        assertEquals(4, notifiedCount);
        assertEquals(1, allGoalsSubject.getValue().size());

        // Check that fields were updated on both subjects
        assertEquals("goalTextChanged", allGoalsSubject.getValue().get(0).getGoalText());
        assertEquals((Integer) 2, allGoalsSubject.getValue().get(0).getSortOrder());
        assertEquals("goalTextChanged", goalSubject.getValue().getGoalText());
        assertEquals((Integer) 2, goalSubject.getValue().getSortOrder());
    }

    @Test
    public void observeSaveMultipleGoals() {
        Subject<List<Goal>> allGoalsSubject = goalRepository.findAll();
        notifiedCount = 0;
        allGoalsSubject.observe(goals -> {
            notifiedCount++;
        });
        List<Goal> goalsToSave = List.of(
                new Goal(null, "goal1", 1),
                new Goal(null, "goal2", 2)
        );
        goalRepository.save(goalsToSave);

        // Our observer should have been called once upon creation, and again upon saving the goals.
        assertEquals(2, notifiedCount);
        assertEquals(2, allGoalsSubject.getValue().size());

        // Ensure that the save() call propogated to the database
        assertEquals(2, goalsDao.count());
    }
}
