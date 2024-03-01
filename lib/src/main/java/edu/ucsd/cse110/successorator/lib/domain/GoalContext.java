package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;
import java.util.Objects;

public class GoalContext {
    private final String name;
    private final Integer id;
    private final String color;

    public GoalContext(String name, Integer id, String color){
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public String getName() {
        return name;
    }


    public Integer getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public char getFirstLetterOfName(){
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

    public static List <GoalContext> getDeafultGoalContext(){
        return List.of(
                new GoalContext("Home", 1, "#ffff00"),
                new GoalContext("Work", 2, "#40e0d0"),
                new GoalContext("School", 3, "#ffc0cb"),
                new GoalContext("Errands", 4, "#7cfc00")
        );
    }
}
