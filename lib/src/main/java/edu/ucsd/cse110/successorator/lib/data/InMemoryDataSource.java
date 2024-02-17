package edu.ucsd.cse110.successorator.lib.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and flashcards that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = 0;

    private final Map<Integer, Goal> goals
            = new HashMap<>();
    private final Map<Integer, SimpleSubject<Goal>> goalSubjects
            = new HashMap<>();
    private final SimpleSubject<List<Goal>> allGoalsSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() { }

    public final static List<Goal> DEFAULT_GOALS = List.of();

    /**
     * @return data from default cards
     */
    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putGoals(DEFAULT_GOALS);
        return data;
    }

    /**
     * @return list of goals
     */
    public List<Goal> getGoals() {
        return List.copyOf(goals.values());
    }

    /**
     * return goal by id
     * @param id of goal to return
     * @return goal with id
     */
    public Goal getGoal(int id) {
        return goals.get(id);
    }

    /**
     * return Subject<Goal> with id
     * @param id of goal
     */
    public Subject<Goal> getGoalSubject(int id) {
        if (!goalSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Goal>();
            subject.setValue(getGoal(id));
            goalSubjects.put(id, subject);
        }
        return goalSubjects.get(id);
    }

    public Subject<List<Goal>> getAllGoalsSubject() {
        return allGoalsSubject;
    }


    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    /**
     * Add goal to list
     * @param goal to be added to list
     */
    public void putGoal(Goal goal) {
        var fixedCard = preInsert(goal);

        goals.put(fixedCard.getId(), fixedCard);
        postInsert();
        assertSortOrderConstraints();

        if (goalSubjects.containsKey(fixedCard.getId())) {
            goalSubjects.get(fixedCard.getId()).setValue(fixedCard);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void putGoals(List<Goal> cards) {
        var fixedCards = cards.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedCards.forEach(card -> goals.put(card.getId(), card));
        postInsert();
        assertSortOrderConstraints();

        fixedCards.forEach(card -> {
            if (goalSubjects.containsKey(card.getId())) {
                goalSubjects.get(card.getId()).setValue(card);
            }
        });
        allGoalsSubject.setValue(getGoals());
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * cards inserted have an id, and updates the nextId if necessary.
     */
    private Goal preInsert(Goal card) {
        var id = card.getId();
        if (id == null) {
            // If the card has no id, give it one.
            card = card.withId(nextId++);
        }
        else if (id >= nextId) {
            // If the card has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load cards like in fromDefault().
            nextId = id + 1;
        }

        return card;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = goals.values().stream()
                .map(Goal::getSortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = goals.values().stream()
                .map(Goal::getSortOrder)
                .max(Integer::compareTo)
                .orElse(0);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = goals.values().stream()
                .map(Goal::getSortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }

    public void changeIsCompleteStatus(Integer id) {
        Goal goal = goals.get(id);
        goal.changeIsCompleteStatus();
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        Goal goal = goals.get(id);
        if(goal.getIsComplete()) {
            goal.setDateCompleted(dateCompleted);
        } else {
            goal.setDateCompleted(null);
        }
    }

    public void changeIsDisplayedStatus(Integer id, boolean isDisplayed) {
        //not sure what this should do rn
    }
}
