package edu.ucsd.cse110.successorator.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class SuccessoratorApplication extends Application {
    private GoalRepository goalRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                    getApplicationContext(),
                    SuccessoratorDatabase.class,
                    "successorator-database"
                )
                .allowMainThreadQueries()
                .build();

        this.goalRepository = new RoomGoalRepository(database.goalsDao());
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
