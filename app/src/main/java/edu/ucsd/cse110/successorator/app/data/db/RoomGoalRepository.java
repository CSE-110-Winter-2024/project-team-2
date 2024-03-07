package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.Transformations;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class RoomGoalRepository implements GoalRepository {
    private final GoalsDao goalsDao;

    public RoomGoalRepository(GoalsDao goalsDao) {
        this.goalsDao = goalsDao;
    }

    @Override
    public Subject<Goal> find(int id) {
        var entityLiveData = goalsDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        var entitiesLiveData = goalsDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getAllGoals() {
        var entitiesLiveData = goalsDao.getAllGoalsAsLiveData();
        var allGoalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(allGoalsLiveData);
    }

    public List<Goal> queryAllGoals() {
        return goalsDao.findAll().stream()
                .map(GoalEntity::toGoal)
                .collect(Collectors.toList());
    }

    public void save(Goal goal) {
        goalsDao.insert(GoalEntity.fromGoal(goal));
    }

    public void save(List<Goal> goals) {
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
        goalsDao.insert(entities);
    }

    @Override
    public void append(Goal goal) {
        goalsDao.append(GoalEntity.fromGoal(goal));
    }

    @Override
    public void changeIsCompleteStatus(Integer id, Calendar date) {
        goalsDao.changeIsCompleteStatus(id);
        // If goal is pending, then crossing it off should move it to Today's list
        if (goalsDao.getIsPendingStatus(id)) {
            goalsDao.changeIsPendingStatus(id, false);
            goalsDao.setGoalDate(id, date);
        }
    }

    @Override
    public void moveToTop(Integer id) {
        goalsDao.moveToTop(id);
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        goalsDao.setDateCompleted(id, dateCompleted);
    }

    @Override
    public void changeIsDisplayedStatus(Integer id, boolean isDisplayed) {
        goalsDao.changeIsDisplayedStatus(id, isDisplayed);
    }

    public void setNextRecurrence(Integer id, Calendar nextRecurrence){
        goalsDao.setNextRecurrence(id, nextRecurrence);
    }

    public void setPastRecurrenceId(Integer id, Integer pastRecurrenceId) {
        goalsDao.setPastRecurrenceId(id, pastRecurrenceId);
    }
}
