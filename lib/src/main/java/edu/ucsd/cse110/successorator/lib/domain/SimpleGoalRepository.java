package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;

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

    public void save(Goal goal) { }

    public void save(List<Goal> goals) { }

    /**
     * Append goal to end of list
     * @param goal the goal to append
     */
    @Override
    public void append(Goal goal) {
        dataSource.putGoal(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    @Override
    public void changeIsCompleteStatus(Integer id) {
        dataSource.changeIsCompleteStatus(id);
    }

    @Override
    public void moveToTop(Integer id) {
        //
    }

    @Override
    public void moveToTop(Integer id) {
        //
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        dataSource.setDateCompleted(id, dateCompleted);
    }

    public void changeIsDisplayedStatus(Integer id, boolean isDisplayed) {
        dataSource.changeIsDisplayedStatus(id, isDisplayed);
    }
}
