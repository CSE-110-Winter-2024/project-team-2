package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.util.views.GoalFormatter;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class GoalFactory {
    public Goal makeRecurringTemplate(String goalText, Calendar goalDate, ViewOptions view, Integer contextId, Goal.RecurrencePattern recurrencePattern) {
        String recurGoalText = new GoalFormatter().recurGoalText(goalDate, goalText, recurrencePattern);
        return new Goal(null, recurGoalText, -1, false, null,
                false, goalDate, false, GoalContext.getGoalContextById(contextId), Goal.RecurType.RECURRING_TEMPLATE,
                recurrencePattern, null, null, null);
    }

    public Goal makeRecurringInstance(String goalText, Calendar goalDate, Integer contextId, Goal.RecurrencePattern recurrencePattern, Integer recurGoalId) {
        return new Goal(null, goalText, -1, false, null,
                false, goalDate, false, GoalContext.getGoalContextById(contextId), Goal.RecurType.RECURRING_INSTANCE,
                recurrencePattern, null, null, recurGoalId);
    }

    public Goal makeOneTimeGoal(String goalText, Calendar goalDate, ViewOptions view, Integer contextId) {
        return new Goal(null, goalText, -1, false, null,
                false, goalDate, view == ViewOptions.PENDING, GoalContext.getGoalContextById(contextId), Goal.RecurType.NOT_RECURRING,
                Goal.RecurrencePattern.NONE, null, null, null);
    }
}
