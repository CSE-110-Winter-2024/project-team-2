package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Subject<Goal> find(int id);
    Goal rawFind(int id);

    Subject<List<Goal>> findAll();

    Subject<List<Goal>> getAllGoals();

    List<Goal> queryAllGoals();

    void save(Goal goal);

    void save(List<Goal> goals);

    int append(Goal goal);


    boolean getIsPendingStatus(Integer id);

    void changeIsPendingStatus(Integer id, boolean isPending);

    void setGoalDate(Integer id, Calendar goalDate);

    void changeIsCompleteStatus(Integer id);

    void moveFromPending(Integer id, Calendar date);

    void moveToTop(Integer id);

    void setDateCompleted(Integer id, Calendar dateCompleted);

    void changeIsDisplayedStatus(Integer id, boolean isDisplayed);

    void setNextRecurrence(Integer id, Calendar nextRecurrence);

    void setPastRecurrenceId(Integer id, Integer pastRecurrenceId);

    void deleteGoal(int id);

    List<Goal> findGoalsByTemplateId(int templateId);

    void setTemplateId(int id, Integer templateId);

    Subject<List<Goal>> findAllSortedByContext();
}
