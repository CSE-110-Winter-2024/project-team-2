package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Subject<Goal> find(int id);

    Subject<List<Goal>> findAll();
    public void save(Goal goal);
    public void save(List<Goal> goals);

    void append(Goal goal);
}
