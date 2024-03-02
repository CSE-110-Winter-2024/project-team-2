package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Objects;

public class Goal {
    public final @NonNull String goalText;
    public final @Nullable Integer id;
    public final @NonNull Integer sortOrder;
    public @NonNull Boolean isComplete;
    public @Nullable Calendar dateCompleted;
    public @NonNull Boolean isDisplayed;
    public @NonNull GoalContext goalContext;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder,
                @NonNull Boolean isComplete, @Nullable Calendar dateCompleted, @NonNull Boolean isDisplayed,
                @NonNull GoalContext goalContext) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
        this.isDisplayed = isDisplayed;
        this.goalContext = goalContext;
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

    public @NonNull Boolean getIsDisplayed() {
        return isDisplayed;
    }

    public @NonNull GoalContext getGoalContext() {
        return goalContext;
    }

    public void changeIsCompleteStatus() {
        this.isComplete = !this.isComplete;
    }

    public void setDateCompleted(@Nullable Calendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void setIsDisplayed(@NonNull Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    public void updateIsDisplayed(Calendar currDate) {
        boolean completedOnCurrDate;
        if (dateCompleted != null) {
            Calendar currDateCopy = (Calendar) currDate.clone();
            Calendar dateCompletedCopy = (Calendar) dateCompleted.clone();

            // Subtract 2 hours to account for 2 AM day change
            currDateCopy.add(Calendar.HOUR, -2);
            dateCompletedCopy.add(Calendar.HOUR, -2);

            completedOnCurrDate = (dateCompletedCopy.get(Calendar.MONTH) == currDateCopy.get(Calendar.MONTH))
                    && (dateCompletedCopy.get(Calendar.DAY_OF_MONTH) == currDateCopy.get(Calendar.DAY_OF_MONTH))
                    && (dateCompletedCopy.get(Calendar.YEAR) == currDateCopy.get(Calendar.YEAR));
        } else {
            completedOnCurrDate = false;
        }

        isDisplayed = (!isComplete || completedOnCurrDate);
    }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalContext);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalContext);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(goalText, goal.goalText) && Objects.equals(id, goal.id) && Objects.equals(sortOrder, goal.sortOrder)
                && Objects.equals(isComplete, goal.isComplete) && Objects.equals(dateCompleted, goal.dateCompleted) && Objects.equals(isDisplayed, goal.isDisplayed)
                && Objects.equals(goalContext, goal.goalContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder, isComplete, dateCompleted, isDisplayed, goalContext);
    }
}
