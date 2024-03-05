package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;

@Entity(tableName = "goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "goalText")
    public String goalText;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "isComplete")
    public boolean isComplete;

    @ColumnInfo(name = "dateCompleted")
    public Calendar dateCompleted;

    @ColumnInfo(name = "isDisplayed")
    public boolean isDisplayed;

    @ColumnInfo(name = "goalDate")
    public Calendar goalDate;

    @ColumnInfo(name = "isPending")
    public boolean isPending;

    @ColumnInfo(name = "contextId")
    public int contextId;

    @ColumnInfo(name = "isRecurring")
    public boolean isRecurring;

    @ColumnInfo(name = "recurrencePattern")
    public Goal.RecurrencePattern recurrencePattern;

    @ColumnInfo(name = "nextRecurrence")
    public Calendar nextRecurrence;

    public GoalEntity(@NonNull String goalText, int sortOrder, boolean isComplete,
                      @Nullable Calendar dateCompleted, boolean isDisplayed,
                      @Nullable Calendar goalDate, boolean isPending, int contextId,
                      boolean isRecurring, Goal.RecurrencePattern recurrencePattern, @Nullable Calendar nextRecurrence) {
        this.goalText = goalText;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
        this.isDisplayed = isDisplayed;
        this.goalDate = goalDate;
        this.isPending = isPending;
        this.contextId = contextId;
        this.isRecurring = isRecurring;
        this.recurrencePattern = recurrencePattern;
        this.nextRecurrence = nextRecurrence;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal) {
        var goalEntity = new GoalEntity(goal.getGoalText(), goal.getSortOrder(), goal.getIsComplete(),
                goal.getDateCompleted(), goal.getIsDisplayed(), goal.getGoalDate(), goal.getIsPending(),
                Objects.requireNonNull(goal.getGoalContext().getId()), goal.getIsRecurring(),goal.getRecurrencePattern(), goal.getNextRecurrence());
        goalEntity.id = goal.getId();
        return goalEntity;
    }

    public @NonNull Goal toGoal() {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, Objects.requireNonNull(GoalContext.getGoalContextById(contextId)), isRecurring, recurrencePattern, nextRecurrence);
    }
}
