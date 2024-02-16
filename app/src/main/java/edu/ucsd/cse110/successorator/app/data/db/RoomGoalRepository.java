package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.LiveData;
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
    public void changeIsCompleteStatus(Integer id) {
        GoalEntity goalEntity = goalsDao.find(id);
        if (goalEntity != null){
            goalEntity.isComplete = !goalEntity.isComplete;
            if(goalEntity.isComplete){
                goalEntity.dateCompleted = Calendar.getInstance();
            } else{
                goalEntity.dateCompleted = null;
            }
            goalsDao.updateGoal(goalEntity);
        }
    }

    public LiveData<List<Goal>> getActiveGoals() {
        return Transformations.map(goalsDao.getActiveGoals(), entities -> {
            return entities.stream().map(GoalEntity::toGoal).collect(Collectors.toList());
        });
    }

    @Override
    public Subject<List<Goal>> getActiveGoalsSubject() {
        LiveData<List<Goal>> activeGoalsLiveData = getActiveGoals();
        return new LiveDataSubjectAdapter<>(activeGoalsLiveData);
    }

}
