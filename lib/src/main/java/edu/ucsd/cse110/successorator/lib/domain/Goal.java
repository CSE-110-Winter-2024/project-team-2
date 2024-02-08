package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Goal {
    public final @NonNull String goalText;
    public final @Nullable Integer id;
    public final @NonNull Integer sortOrder;

    public Goal(@Nullable Integer id, @NonNull String goalText, @NonNull Integer sortOrder ) {
        this.goalText = goalText;
        this.id = id;
        this.sortOrder = sortOrder;
    }

    public @NonNull String getGoalText() {
        return goalText;
    }

    public @Nullable Integer getId() { return id; }

    public @NonNull Integer getSortOrder() { return sortOrder; }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goalText, sortOrder);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goalText, sortOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal1 = (Goal) o;
        return Objects.equals(goalText, goal1.goalText) && Objects.equals(id, goal1.id) && Objects.equals(sortOrder, goal1.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalText, id, sortOrder);
    }
}
