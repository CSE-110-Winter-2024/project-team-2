package edu.ucsd.cse110.successorator.lib.util.views;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.date.DateFormatter;

public class GoalFormatter {
    public String recurGoalText(Calendar date, String goalText, Goal.RecurrencePattern recurrencePattern) {
        if(Goal.RecurrencePattern.DAILY == recurrencePattern) {
            return goalText + ", Daily";
        } else if(Goal.RecurrencePattern.WEEKLY == recurrencePattern) {
            return goalText + ", Weekly on " + new DateFormatter().formatWeekDay(date);
        } else if(Goal.RecurrencePattern.MONTHLY == recurrencePattern) {
            return goalText + ", Monthly on " + new DateFormatter().formatDayOfMonth(date);
        } else if(Goal.RecurrencePattern.YEARLY == recurrencePattern) {
            return goalText + ", Yearly on " + new DateFormatter().formatDayOfYear(date);
        }
        return "";
    }
}
