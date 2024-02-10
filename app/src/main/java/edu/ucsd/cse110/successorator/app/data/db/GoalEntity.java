package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Entity(tableName = "goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "goalText")
    public String goalText;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    public GoalEntity(@NonNull String goalText, int sortOrder) {
        this.goalText = goalText;
        this.sortOrder = sortOrder;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal) {
        var goalEntity = new GoalEntity(goal.getGoalText(), goal.getSortOrder());
        goalEntity.id = goal.getId();
        return goalEntity;
    }

    public @NonNull Goal toGoal() {
        return new Goal(id, goalText, sortOrder);
    }
}
