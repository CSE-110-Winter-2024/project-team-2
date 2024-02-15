package edu.ucsd.cse110.successorator.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.app.data.db.DateRepository;
import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.app.util.CurrentDateProvider;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class SuccessoratorApplication extends Application {
    private GoalRepository goalRepository;
    private DateRepository dateRepository;

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
        this.dateRepository = new DateRepository(new CurrentDateProvider());
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }

    public DateRepository getDateRepository() {
        return dateRepository;
    }
}
