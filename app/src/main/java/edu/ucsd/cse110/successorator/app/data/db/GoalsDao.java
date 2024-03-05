package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Calendar;
import java.util.List;

@Dao
public interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<GoalEntity> goals);

    @Query("SELECT * FROM goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM goals WHERE isDisplayed = True ORDER BY isComplete, sort_order")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals ORDER BY isComplete, sort_order")
    List<GoalEntity> getAllGoals();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals WHERE isDisplayed = True ORDER BY isComplete, sort_order ")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM goals ORDER BY isComplete, sort_order")
    LiveData<List<GoalEntity>> getAllGoalsAsLiveData();

    @Query("SELECT COUNT(*) FROM goals")
    int count();

    @Query("SELECT MIN(sort_order) FROM goals")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET isComplete = NOT isComplete WHERE id = :id")
    void changeIsCompleteStatus(int id);

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);

    @Query("UPDATE goals SET isDisplayed = :isDisplayed WHERE id = :id")
    void changeIsDisplayedStatus(Integer id, boolean isDisplayed);

    // Sets date completed to the given date if the goal is marked as complete. If the goal is not
    // marked as complete, sets date completed to null.
    @Query("UPDATE goals SET dateCompleted = (CASE WHEN isComplete = True THEN :dateCompleted ELSE " +
            "NULL END) WHERE id = :id")
    void setDateCompleted(Integer id, Calendar dateCompleted);

    @Query("SELECT isPending FROM goals where id = :id")
    boolean getIsPendingStatus(int id);

    @Query("UPDATE goals SET isPending = :isPending WHERE id = :id")
    void changeIsPendingStatus(Integer id, boolean isPending);

    @Query("UPDATE goals SET goalDate = :goalDate WHERE id = :id")
    void setGoalDate(Integer id, Calendar goalDate);

    @Query("UPDATE goals SET nextRecurrence = :nextRecurrence WHERE id = :id")
    void setNextRecurrence(Integer id, Calendar nextRecurrence);

    @Transaction
    default int append(GoalEntity goal) {
        var maxSortOrder = getMaxSortOrder();
        var newGoal = new GoalEntity(goal.goalText, maxSortOrder + 1, goal.isComplete, null,
                true, goal.goalDate, goal.isPending, goal.contextId, goal.isRecurring, goal.recurrencePattern, goal.nextRecurrence);
        return Math.toIntExact(insert(newGoal));
    }

    @Transaction
    default int moveToTop(Integer id) {
        GoalEntity goal = find(id);
        goal.sortOrder = getMinSortOrder() - 1;
        delete(id);
        return Math.toIntExact(insert(goal));
    }
}
