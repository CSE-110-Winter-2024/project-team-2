package edu.ucsd.cse110.successorator.lib.domain;

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
     * Append goal to end of list
     * @param goal the goal to append
     */
    @Override
    public void append(Goal goal) {
        dataSource.putGoal(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
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