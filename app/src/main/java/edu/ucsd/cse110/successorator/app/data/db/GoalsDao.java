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

    // The complicated ORDER BY clause will order goals from oldest to newest for recurring templates only
    @Query("SELECT * FROM goals WHERE isDisplayed = True ORDER BY isComplete, (CASE WHEN recurType = 'RECURRING_TEMPLATE' THEN goalDate ELSE sort_order END)")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals ORDER BY isComplete, sort_order")
    List<GoalEntity> getAllGoals();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals WHERE isDisplayed = True ORDER BY isComplete, (CASE WHEN recurType = 'RECURRING_TEMPLATE' THEN goalDate ELSE sort_order END)")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM goals ORDER BY isComplete, sort_order")
    LiveData<List<GoalEntity>> getAllGoalsAsLiveData();

    @Query("SELECT COUNT(*) FROM goals")
    int count();

    @Query("SELECT MIN(sort_order) FROM goals")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM goals")
    int getMaxSortOrder();

    /*
     * Ensures that any future occurrences of the goal with the given ID are NOT completed. Handles
     * the edge case where the today instance of a recurring goal is completed, then the tomorrow instance is
     * completed, then the today instance is un-completed, we should also set the tomorrow instance to un-completed
     */
    @Query("UPDATE goals SET isComplete = False, dateCompleted = NULL WHERE pastRecurrenceId = :id")
    void ensureFutureGoalsNotCompleted(int id);

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

    @Query("UPDATE goals SET pastRecurrenceId = :pastRecurrenceId WHERE id = :id")
    void setPastRecurrenceId(Integer id, Integer pastRecurrenceId);

    @Transaction
    default int append(GoalEntity goal) {
        var maxSortOrder = getMaxSortOrder();
        var newGoal = new GoalEntity(goal.goalText, maxSortOrder + 1, goal.isComplete, null,
                goal.isDisplayed, goal.goalDate, goal.isPending, goal.contextId, goal.recurType, goal.recurrencePattern,
                goal.nextRecurrence, goal.pastRecurrenceId, goal.templateId);
        return Math.toIntExact(insert(newGoal));
    }

    @Transaction
    default int moveToTop(Integer id) {
        GoalEntity goal = find(id);
        goal.sortOrder = getMinSortOrder() - 1;
        delete(id);
        return Math.toIntExact(insert(goal));
    }

    @Query("SELECT * FROM goals WHERE templateId = :templateId")
    List<GoalEntity> findGoalsByTemplateId(int templateId);

    @Query("UPDATE goals SET templateId = :templateId WHERE id = :id")
    void setTemplateId(int id, Integer templateId);

    @Query("SELECT * FROM goals " +
            // Only select displayed goals
            "WHERE isDisplayed = True ORDER BY " +

            // Completed goals should always be at the end
            "isComplete, " +

            // Order by context ID, but only for goals that are not completed and not recurring templates
            "(CASE WHEN isComplete = false AND recurType != 'RECURRING_TEMPLATE' THEN contextId ELSE NULL END), " +

            // Order recurring templates from earliest to latest start date; order others by sort order
            "(CASE WHEN recurType = 'RECURRING_TEMPLATE' THEN goalDate ELSE sort_order END)")
    LiveData<List<GoalEntity>> sortByContextAsLiveData();
}
