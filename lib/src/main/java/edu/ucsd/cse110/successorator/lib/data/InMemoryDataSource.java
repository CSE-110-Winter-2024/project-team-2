package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and flashcards that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {

    private final Map<Integer, Goal> goals
            = new HashMap<>();
    private final Map<Integer, SimpleSubject<Goal>> goalSubjects
            = new HashMap<>();
    private final SimpleSubject<List<Goal>> allGoalSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public List<Goal> getGoals() {
        return List.copyOf(goals.values());
    }

    public Goal getGoal(int id) {
        return goals.get(id);
    }

    public SimpleSubject<Goal> getGoalSubject(int id) {
        if (!goalSubjects.containsKey(id)){
            var subject = new SimpleSubject<Goal>();
            subject.setValue(getGoal(id));
            goalSubjects.put(id, subject);
        }
        return goalSubjects.get(id);
    }

    public Subject<List<Goal>> getAllGoalsSubject() {
        return allGoalSubject;
    }

    public void putGoal(Goal goal) {
        goals.put(goal.getId(), goal);
        if(goalSubjects.containsKey(goal.getId())) {
            goalSubjects.get(goal.getId()).setValue(goal);
        }
        allGoalSubject.setValue(getGoals());
    }

    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal(0,"Goal 1", 0),
            new Goal(1,"Goal 2", 1),
            new Goal(2,"Goal 3", 2),
            new Goal(3,"Goal 4", 3),
            new Goal(4,"Goal 5", 4),
            new Goal(5,"Goal 6", 5)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_GOALS) {
            data.putGoal(goal);
        }
        return data;
    }
}
