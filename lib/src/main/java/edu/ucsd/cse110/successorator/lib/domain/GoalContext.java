package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * The GoalContext class represents one of the possible contexts that a goal can have. Each context
 * has a name and color that is displayed in the UI. MS2 requirements have a static set of 4
 * possible contexts, so we hard-code those available contexts as a list.
 */
public class GoalContext {
    private final @NonNull String name;
    private final @Nullable Integer id;
    private final @NonNull String color;

    public GoalContext(@NonNull String name, @Nullable Integer id, @NonNull String color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public @NonNull String getName() {
        return name;
    }


    public @Nullable Integer getId() {
        return id;
    }

    public  @NonNull String getColor() {
        return color;
    }

    public char getFirstLetterOfName() {
        return name.charAt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoalContext)) return false;
        GoalContext that = (GoalContext) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getId(), that.getId()) && Objects.equals(getColor(), that.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getColor());
    }

    // These are the four contexts that are available for a goal.
    public static List<GoalContext> defaultGoalContexts = List.of(
            new GoalContext("Home", 1, "#ffff00"),
            new GoalContext("Work", 2, "#40e0d0"),
            new GoalContext("School", 3, "#ffc0cb"),
            new GoalContext("Errands", 4, "#7cfc00")
    );

    /**
     * Retrieves the goal context associated with the corresponding ID. Only returns a valid result
     * for a contextId between 1 and 4 inclusive.
     *
     * @param contextId the ID of the context we want to retrieve
     * @return The GoalContext instance associated with that ID
     */
    public static @NonNull GoalContext getGoalContextById(int contextId) {
        // Loop over all contexts, checking if they match the desired context ID
        for (GoalContext context : defaultGoalContexts) {
            if (context.getId() == contextId) {
                return context;
            }
        }

        // This point should never be reached if we are given a valid context ID
        assert false;
        return null;
    }
}
