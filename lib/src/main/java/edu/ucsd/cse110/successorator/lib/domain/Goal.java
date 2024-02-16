package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Goal {
    public final @NonNull String goalText;
    public final @Nullable Integer id;
    public final @NonNull Integer sortOrder;
    public @NonNull Boolean isComplete;
    public @Nullable Calendar dateCompleted;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder, @NonNull Boolean isComplete, @Nullable Calendar dateCompleted) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
    }

    public @NonNull String getGoalText() {
        return goalText;
    }

    public @Nullable Integer getId() {
        return id;
    }

    public @NonNull Integer getSortOrder() {
        return sortOrder;
    }

    public @NonNull Boolean getIsComplete() {
        return isComplete;
    }

    public @Nullable Calendar getDateCompleted() {
        return dateCompleted;
    }

    public void changeIsCompleteStatus() {
        this.isComplete = !this.isComplete;
    }

    public void setDateCompleted (Calendar dateCompleted) {
        if(isComplete) {
            this.dateCompleted = dateCompleted;
        } else {
            this.dateCompleted = null;
        }
    }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(goalText, goal.goalText) && Objects.equals(id, goal.id) && Objects.equals(sortOrder, goal.sortOrder)
                && Objects.equals(isComplete, goal.isComplete) && Objects.equals(dateCompleted, goal.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder, isComplete, dateCompleted);
    }
}
