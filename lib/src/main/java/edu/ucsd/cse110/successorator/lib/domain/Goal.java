package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
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
    public @NonNull RecurType recurType;
    public @NonNull GoalContext goalContext;
    public RecurrencePattern recurrencePattern;
    public Calendar nextRecurrence;
    public @Nullable Integer pastRecurrenceId;

    @Nullable Integer templateId;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder,
                @NonNull Boolean isComplete, @Nullable Calendar dateCompleted, @NonNull Boolean isDisplayed,
                @Nullable Calendar goalDate, @NonNull Boolean isPending, @NonNull GoalContext goalContext,
                @NonNull RecurType recurType, @NonNull RecurrencePattern recurrencePattern,
                @Nullable Calendar nextRecurrence, @Nullable Integer pastRecurrenceId, @Nullable Integer templateId) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
        this.isComplete = isComplete;
        this.dateCompleted = dateCompleted;
        this.isDisplayed = isDisplayed;
        this.goalDate = goalDate;
        this.isPending = isPending;
        this.recurType = recurType;
        this.recurrencePattern = recurrencePattern;
        this.nextRecurrence = nextRecurrence;
        this.goalContext = goalContext;
        this.pastRecurrenceId = pastRecurrenceId;
        this.templateId = templateId;
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

    @NonNull
    public RecurType getRecurType() {
        return recurType;
    }

    @NonNull
    public RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    public Calendar getNextRecurrence() {
        return nextRecurrence;
    }

    public @Nullable Integer getPastRecurrenceId() {
        return pastRecurrenceId;
    }

    public @Nullable Integer getTemplateId() { return templateId; }

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

    public void setRecurType(@NonNull RecurType recurType) {
        this.recurType = recurType;
    }

    public void setRecurrencePattern(@NonNull RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public void setNextRecurrence(@Nullable Calendar nextRecurrence) {
        this.nextRecurrence = nextRecurrence;
    }

    public enum RecurrencePattern {
        NONE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public enum RecurType {
        NOT_RECURRING, // Goal is not recurring at all
        RECURRING_TEMPLATE, // Goal is a template for making recurring goals
        RECURRING_INSTANCE // Goal is an instance of a recurring goal template
    }

    public void updateIsDisplayed(Calendar date, ViewOptions view, GoalContext context) {
        if (view == ViewOptions.PENDING) {
            isDisplayed = isPending;
        } else if (view == ViewOptions.RECURRING) {
            // Only show recurring templates in recurring view
            isDisplayed = (recurType == Goal.RecurType.RECURRING_TEMPLATE);
        } else if (view == ViewOptions.TODAY) {
            isDisplayed = !isPending // don't display pending goals on today's list
                    && goalDate != null // must have a goalDate
                    && !(isComplete
                        && dateCompleted != null
                        && new DateComparer().compareDates(new MockDateProvider(dateCompleted).getCurrentViewDate(view), date) < 0
                    ) // do not display if the goal was completed on a previous day
                    // goal date must be for past or present, not future (future is tomorrow's list!)
                    && new DateComparer().compareDates(goalDate, date) <= 0
                    && recurType != Goal.RecurType.RECURRING_TEMPLATE // do not display template goals
                    // do not display goal if the next occurrence of the goal is displayed
                    && (nextRecurrence == null || new DateComparer().compareDates(nextRecurrence, date) > 0);
        } else if (view == ViewOptions.TOMORROW) {
            isDisplayed = !isPending // don't display pending goals on tomorrow's list
                    && goalDate != null // must have a goalDate
                    && !(isComplete
                        && dateCompleted != null
                    && new DateComparer().compareDates(new MockDateProvider(dateCompleted).getCurrentViewDate(view), date) < 0
                    ) // do not display if the goal was completed on a previous day
                    // goal date must be for tomorrow's date, not before or after that
                    && new DateComparer().compareDates(goalDate, date) == 0
                    && recurType != Goal.RecurType.RECURRING_TEMPLATE // do not display template goals
                    // do not display goal if the next occurrence of the goal is displayed
                    && (nextRecurrence == null || new DateComparer().compareDates(nextRecurrence, date) > 0);
        }

        if (context != null && !Objects.equals(context.getId(), goalContext.getId())) {
            isDisplayed = false;
        }
    }

    public Calendar calculateNextRecurrenceDate(Goal templateGoal) {
        Calendar newGoalDate = (Calendar) this.getGoalDate().clone();
        if(this.getRecurrencePattern() == RecurrencePattern.DAILY) {
            // if DAILY goal, next occurrence date is day after this goalDate
            newGoalDate.add(Calendar.DATE, 1);
        } else if (this.getRecurrencePattern() == RecurrencePattern.WEEKLY) {
            // if WEEKLY goal, next occurrence is week after this goalDate
            newGoalDate.add(Calendar.WEEK_OF_YEAR, 1);
        } else if (this.getRecurrencePattern() == RecurrencePattern.MONTHLY) {
            assert templateGoal != null && templateGoal.getGoalDate() != null;
            int targetDOWInMonth = templateGoal.getGoalDate().get(Calendar.DAY_OF_WEEK_IN_MONTH);
            boolean isFifthOfMonth = targetDOWInMonth == 5;
            if (isFifthOfMonth) {
                // If goal recurs on 5th DOW of month, go only until 4th...
                targetDOWInMonth--;
            }

            // if MONTHLY goal, next occurrence is the next time that day_of_week_in_month matches this goalDate
            do {
                newGoalDate.add(Calendar.WEEK_OF_YEAR, 1);
            } while (newGoalDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) != targetDOWInMonth);
            if (isFifthOfMonth) {
                //...And then add 1 more week
                newGoalDate.add(Calendar.WEEK_OF_YEAR, 1);
            }
        } else if (this.getRecurrencePattern() == RecurrencePattern.YEARLY) {
            // if YEARLY goal, next occurrence is year after this goalDate
            newGoalDate.add(Calendar.YEAR, 1);

            DateComparer dateComparer = new DateComparer();
            /*
             * If this year is a leap year and next year is not, add 1 day to move next year's goal to March 1
             */
            boolean isLeapDay = dateComparer.isLeapDay(getGoalDate());
            if (isLeapDay) {
                newGoalDate.add(Calendar.DATE, 1);
            }

            /*
             * If next year is a leap year and our goal is supposed to be on leap day, adjust from March 1 to February 29
             */
            if (templateGoal != null && templateGoal.getGoalDate() != null &&
                    dateComparer.isLeapDay(templateGoal.getGoalDate())) {
                newGoalDate.add(Calendar.DATE, -1);
                if (!dateComparer.isLeapDay(newGoalDate)) {
                    // If next year isn't a leap year, go forward from Feb 28 to March 1
                    newGoalDate.add(Calendar.DATE, 1);
                }
            }
        }
        return newGoalDate;
    }

    public Calendar updateNextRecurrence(Goal templateGoal) {
        Calendar newGoalDate = calculateNextRecurrenceDate(templateGoal);

        this.setNextRecurrence(newGoalDate);

        return newGoalDate;
    }

    public Goal makeNextOccurrence(Goal templateGoal) {
        return new Goal(null, this.getGoalText(), this.getSortOrder(), false,
                null,false, this.getNextRecurrence(),false, this.getGoalContext(),
                Goal.RecurType.RECURRING_INSTANCE, this.getRecurrencePattern(), null, id, templateId);
    }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, goalContext, recurType, recurrencePattern, nextRecurrence, pastRecurrenceId, templateId);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate, isPending, goalContext, recurType, recurrencePattern, nextRecurrence, pastRecurrenceId, templateId);
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
                && Objects.equals(goalContext, goal.goalContext) && Objects.equals(recurType, goal.recurType) && Objects.equals(recurrencePattern,goal.recurrencePattern)
                && Objects.equals(nextRecurrence, goal.nextRecurrence) && Objects.equals(pastRecurrenceId, goal.pastRecurrenceId) && Objects.equals(templateId, goal.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder, isComplete, dateCompleted, isDisplayed, goalDate,
                isPending, goalContext, recurType, recurrencePattern, nextRecurrence, pastRecurrenceId, templateId);
    }
}
