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

    @Query("SELECT * FROM goals ORDER BY isComplete, sort_order")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals WHERE isDisplayed = True ORDER BY isComplete, sort_order ")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM goals")
    int count();

    @Query("SELECT MIN(sort_order) FROM goals")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET isComplete = NOT isComplete WHERE id = :id")
    void changeIsCompleteStatus(Integer id);

    @Query("UPDATE goals SET isDisplayed = :is_Displayed WHERE id = :id")
    void changeIsDisplayedStatus(Integer id, boolean is_Displayed);

    @Query("UPDATE goals SET dateCompleted = :date_Completed WHERE id = :id")
    void setDateCompleted(Integer id, Calendar date_Completed);

    @Transaction
    default int append(GoalEntity goal) {
        var maxSortOrder = getMaxSortOrder();
        var newGoal = new GoalEntity(goal.goalText, maxSortOrder + 1, goal.isComplete, null, true);
        return Math.toIntExact(insert(newGoal));
    }
}
