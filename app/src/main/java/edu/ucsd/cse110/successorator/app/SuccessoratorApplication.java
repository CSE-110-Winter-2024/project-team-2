package edu.ucsd.cse110.successorator.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.app.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.FocusModeRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.date.CurrentDateProvider;

public class SuccessoratorApplication extends Application {
    private GoalRepository goalRepository;
    private DateRepository dateRepository;
    private ViewRepository viewRepository;
    private FocusModeRepository focusModeRepository;

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
        this.viewRepository = new ViewRepository();
        this.focusModeRepository = new FocusModeRepository();
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }

    public DateRepository getDateRepository() {
        return dateRepository;
    }

    public ViewRepository getViewRepository() {
        return viewRepository;
    }

    public FocusModeRepository getFocusModeRepository() {
        return focusModeRepository;
    }
}
