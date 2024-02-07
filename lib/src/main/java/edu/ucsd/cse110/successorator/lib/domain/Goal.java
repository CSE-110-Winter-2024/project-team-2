package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Goal {
    public final @NonNull String goal;
    public final @NonNull Integer id;
    public final @NonNull Integer sortOrder;

    public Goal(@NonNull Integer id, @NonNull String goal, @NonNull Integer sortOrder ) {
        this.goal = goal;
        this.id = id;
        this.sortOrder = sortOrder;
    }

    public @NonNull String getGoal() {
        return goal;
    }

    public @NonNull Integer getId() { return id; }

    public @NonNull Integer getSortOrder() { return sortOrder; }

    /**
     * set sortOrder of Goal
     * @param sortOrder to set
     * @return goal with sortOrder
     */
    public @NonNull Goal withSortOrder(Integer sortOrder) {
        return new Goal(id, goal, sortOrder);
    }

    /**
     * set id of Goal
     * @param id to set
     * @return goal with id
     */
    public @NonNull Goal withId(Integer id) {
        return new Goal(id, goal, sortOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal1 = (Goal) o;
        return Objects.equals(goal, goal1.goal) && Objects.equals(id, goal1.id) && Objects.equals(sortOrder, goal1.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goal, id, sortOrder);
    }
}
