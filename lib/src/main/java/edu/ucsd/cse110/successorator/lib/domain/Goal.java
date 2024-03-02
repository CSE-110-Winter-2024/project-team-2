package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class Goal {
    public final @NonNull String goalText;
    public final @Nullable Integer id;
    public final @NonNull Integer sortOrder;
    public @NonNull Boolean isComplete;
    public @Nullable Calendar dateCompleted;
    public @NonNull Boolean isDisplayed;
    public @Nullable Calendar goalDate;
    public @NonNull Boolean isPending;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder,
                @NonNull Boolean isComplete, @Nullable Calendar dateCompleted, @NonNull Boolean isDisplayed,
                @Nullable Calendar goalDate, @NonNull Boolean isPending) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
        this.isDisplayed = isDisplayed;
        this.goalDate = goalDate;
        this.isPending = isPending;
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

    public @Nullable Calendar getGoalDate() {
        return goalDate;
    }

    public @NonNull Boolean getIsPending() {
        return isPending;
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

    public void setGoalDate(@Nullable Calendar goalDate) {
        this.goalDate = goalDate;
    }

    public void setIsPending(@NonNull Boolean isPending) {
        this.isPending = isPending;
    }

    public void updateIsDisplayed(Calendar date, ViewOptions view) {
        if (view == ViewOptions.PENDING) {
            isDisplayed = isPending;
        } else if (view == ViewOptions.RECURRING) {
            isDisplayed = false;
        } else if (view == ViewOptions.TODAY || view == ViewOptions.TOMORROW) {
            if (goalDate != null) {
                // Goal is displayed if dates match
                isDisplayed = (goalDate.get(Calendar.MONTH) == date.get(Calendar.MONTH))
                        && (goalDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH))
                        && (goalDate.get(Calendar.YEAR) == date.get(Calendar.YEAR));
                // Case where goal is not complete and rolls over
                if (!isComplete && view == ViewOptions.TODAY
                        && new DateComparer().isFirstDateBeforeSecondDate(goalDate, date)) {
                    isDisplayed = true;
                }
            } else {
                isDisplayed = false;
            }
        }
    }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(goalText, goal.goalText) && Objects.equals(id, goal.id) && Objects.equals(sortOrder, goal.sortOrder) && Objects.equals(isComplete, goal.isComplete) && Objects.equals(dateCompleted, goal.dateCompleted) && Objects.equals(isDisplayed, goal.isDisplayed) && Objects.equals(goalDate, goal.goalDate) && Objects.equals(isPending, goal.isPending);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending);
    }
}
