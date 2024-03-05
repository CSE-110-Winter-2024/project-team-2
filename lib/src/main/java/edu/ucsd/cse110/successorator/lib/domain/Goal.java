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
    public @NonNull Boolean isRecurring;
    public @NonNull GoalContext goalContext;
    public RecurrencePattern recurrencePattern;
    public Calendar nextRecurrence;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder,
                @NonNull Boolean isComplete, @Nullable Calendar dateCompleted, @NonNull Boolean isDisplayed,
                @Nullable Calendar goalDate, @NonNull Boolean isPending, @NonNull GoalContext goalContext,
                @NonNull Boolean isRecurring, @NonNull RecurrencePattern recurrencePattern,
                @Nullable Calendar nextRecurrence) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
        this.isDisplayed = isDisplayed;
        this.goalDate = goalDate;
        this.isPending = isPending;
        this.isRecurring = isRecurring;
        this.recurrencePattern = recurrencePattern;
        this.nextRecurrence = nextRecurrence;
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

    public @Nullable Calendar getGoalDate() {
        return goalDate;
    }

    public @NonNull Boolean getIsPending() {
        return isPending;
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

    public void setGoalDate(@Nullable Calendar goalDate) {
        this.goalDate = goalDate;
    }

    public void setIsPending(@NonNull Boolean isPending) {
        this.isPending = isPending;
    }
    public boolean getIsRecurring(){ return isRecurring; }
    public void setIsRecurring(@NonNull Boolean isRecurring){
        this.isRecurring = isRecurring;
    }

    @NonNull
    public RecurrencePattern getRecurrencePattern(){ return recurrencePattern; }

    public void setRecurrencePattern(@NonNull RecurrencePattern recurrencePattern){
        this.recurrencePattern = recurrencePattern;
    }

    public Calendar getNextRecurrence(){return nextRecurrence;}

    public void setNextRecurrence(@Nullable Calendar nextRecurrence){
        this.nextRecurrence = nextRecurrence;
    }

    public enum RecurrencePattern{
        NONE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public void updateIsDisplayed(Calendar date, ViewOptions view) {
        if (view == ViewOptions.PENDING) {
            isDisplayed = isPending;
        } else if (view == ViewOptions.RECURRING) {
            isDisplayed = isRecurring;
        } else if (view == ViewOptions.TODAY) {
            isDisplayed = !isPending // don't display pending goals on today's list
                    && goalDate != null // must have a goalDate
                    && !(isComplete
                        && dateCompleted != null
                        && new DateComparer().compareDates(dateCompleted, date) < 0
                    ) // do not display if the goal was completed on a previous day
                    // goal date must be for past or present, not future (future is tomorrow's list!)
                    && new DateComparer().compareDates(goalDate, date) <= 0
                    && (nextRecurrence == null || new DateComparer().compareDates(nextRecurrence, date) > 0);
        } else if (view == ViewOptions.TOMORROW) {
            Calendar tmrDate = (Calendar) date.clone();
            tmrDate.add(Calendar.DATE, 1);
            isDisplayed = !isPending // don't display pending goals on tomorrow's list
                    && goalDate != null // must have a goalDate
                    // goal date must be for tomorrow's date, not before or after that
                    && new DateComparer().compareDates(goalDate, date) == 0
                    && (nextRecurrence == null || new DateComparer().compareDates(nextRecurrence, tmrDate) > 0);
        }
    }

    public Goal updateNextOccurrence(){
        if (this.getNextRecurrence() == null) {
            Calendar newGoalDate = Calendar.getInstance();
            newGoalDate.setTime(this.getGoalDate().getTime());
            if(this.getRecurrencePattern() == RecurrencePattern.DAILY) {
                newGoalDate.add(Calendar.DATE, 1);
            } else if (this.getRecurrencePattern() == RecurrencePattern.WEEKLY) {
                newGoalDate.add(Calendar.WEEK_OF_YEAR, 1);
            } else if (this.getRecurrencePattern() == RecurrencePattern.MONTHLY) {
                //TODO: need to fix this logic!!
                do {
                    newGoalDate.add(Calendar.WEEK_OF_YEAR, 1);
                } while (newGoalDate.WEEK_OF_MONTH != this.getGoalDate().WEEK_OF_MONTH);
            } else if (this.getRecurrencePattern() == RecurrencePattern.YEARLY) {
                newGoalDate.add(Calendar.YEAR, 1);
            }

            this.setNextRecurrence(newGoalDate);
            return new Goal(this.getId(), this.getGoalText(), this.getSortOrder(), false,
                    null,false, newGoalDate,false, this.getGoalContext(),
                    this.getIsRecurring(),this.getRecurrencePattern(), null);
        }
        return null;
    }

    private boolean matchesRecurrencePattern(Calendar date) {
        switch (recurrencePattern) {
            case DAILY:
                return new DateComparer().compareDates(date, goalDate) < 0;
            case WEEKLY:
                return goalDate.get(Calendar.DAY_OF_WEEK) == date.get(Calendar.DAY_OF_WEEK) &&
                        new DateComparer().compareDates(date, goalDate) < 0;
            case MONTHLY:
                return goalDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
                        new DateComparer().compareDates(date, goalDate) < 0;
            case YEARLY:
                return goalDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                        && goalDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
                        new DateComparer().compareDates(date, goalDate) < 0;
            default:
                return false;
        }
    }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, goalContext, isRecurring, recurrencePattern, nextRecurrence);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, goalContext, isRecurring, recurrencePattern, nextRecurrence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(goalText, goal.goalText) && Objects.equals(id, goal.id)
                && Objects.equals(sortOrder, goal.sortOrder) && Objects.equals(isComplete, goal.isComplete)
                && Objects.equals(dateCompleted, goal.dateCompleted) && Objects.equals(isDisplayed, goal.isDisplayed)
                && Objects.equals(goalDate, goal.goalDate) && Objects.equals(isPending, goal.isPending)
                && Objects.equals(goalContext, goal.goalContext) && Objects.equals(isRecurring, goal.isRecurring) && Objects.equals(recurrencePattern,goal.recurrencePattern)
                && Objects.equals(nextRecurrence, goal.nextRecurrence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, goalContext, isRecurring, recurrencePattern, nextRecurrence);
    }
}
