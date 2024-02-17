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
import edu.ucsd.cse110.successorator.lib.util.date.DateFormatter;

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
    public void changeIsCompleteStatus(Integer id, Calendar dateCompleted) {
        GoalEntity goalEntity = goalsDao.find(id);
        if (goalEntity != null){
            goalEntity.isComplete = !goalEntity.isComplete;
            if(goalEntity.isComplete){
                goalEntity.dateCompleted = new DateFormatter().formatDateDatabase(dateCompleted);
            } else{
                goalEntity.dateCompleted = null;
            }
            goalsDao.updateGoal(goalEntity);
        }
    }

    public LiveData<List<Goal>> getActiveGoals(Calendar mutableDate) {
        String mutableDateFormatted = new DateFormatter().formatDateDatabase(mutableDate);
        System.out.println("\nMutable date is " + mutableDateFormatted + "\n"); // TESTING
        System.out.println(goalsDao.getActiveGoals(mutableDateFormatted).getValue() + " in RoomGoalRepository"); // TESTING

        return Transformations.map(goalsDao.getActiveGoals(mutableDateFormatted), entities -> {
            return entities.stream().map(GoalEntity::toGoal).collect(Collectors.toList());
        });
    }

    @Override
    public Subject<List<Goal>> getActiveGoalsSubject(Calendar mutableDate) {
        LiveData<List<Goal>> activeGoalsLiveData = getActiveGoals(mutableDate);
        return new LiveDataSubjectAdapter<>(activeGoalsLiveData);
    }
}
