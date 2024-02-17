package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;

    private final SimpleSubject<List<Goal>> activeGoalsSubject = new SimpleSubject<>();

    public SimpleGoalRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Goal> find(int id) {
        return dataSource.getGoalSubject(id);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    /**
     * These save methods are no-ops for now since we are not using the SimpleGoalRepository anyway.
     * These methods are just required in order to implement the GoalRepository interface.
     */

    public void save(Goal goal) {

    }

    public void save(List<Goal> goals) {

    }

    /**
     * Append goal to end of list
     *
     * @param goal the goal to append
     */
    @Override
    public void append(Goal goal) {
        dataSource.putGoal(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    @Override
    public void changeIsCompleteStatus(Integer id, Calendar dateCompleted) {
        dataSource.changeIsCompleteStatus(id, dateCompleted);
        updateActiveGoals(dateCompleted); //Update active goals and notify observers
    }

    public Subject<List<Goal>> getActiveGoalsSubject(Calendar mutableDate) {
        updateActiveGoals(mutableDate); // Ensure the subject has the initial state
        return activeGoalsSubject;
    }

    private void updateActiveGoals(Calendar mutableDate){
        List<Goal> activeGoals = getActiveGoals(mutableDate);
        activeGoalsSubject.setValue(activeGoals);
    }

    private List<Goal> getActiveGoals(Calendar mutableDate) {
        return dataSource.getGoals().stream()
            .filter(goal -> !goal.getIsComplete() || isCompletedToday(goal.getDateCompleted()))
            .collect(Collectors.toList());
    }

    private boolean isCompletedToday(String dateCompleted) {
        return true; // Dummy value since we're not using SimpleGoalRepository
        // if (dateCompleted == null) {
        //     return false;
        // }
        // Calendar today = Calendar.getInstance();
        // return dateCompleted.get(Calendar.YEAR) == today.get(Calendar.YEAR)
        //         && dateCompleted.get(Calendar.MONTH) == today.get(Calendar.MONTH)
        //         && dateCompleted.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    // prepend code from lab 5, wasn't working
//    public void prepend(Goal goal) {
//        //shift all existing goals up by one.
//        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
//        // Then insert the new goal before the first one
//        dataSource.putGoal(
//                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
//        );
//    }
}
