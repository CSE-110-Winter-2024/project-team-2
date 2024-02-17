package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;
    private final DateRepository dateRepository;

    // UI state
    private final SimpleSubject<List<Integer>> goalOrdering;
    private final SimpleSubject<List<Goal>> orderedGoals;
    private final SimpleSubject<Calendar> date;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository(), app.getDateRepository());
                    });

    public MainViewModel(GoalRepository goalRepository, DateRepository dateRepository) {
        this.goalRepository = goalRepository;
        this.dateRepository = dateRepository;

        // Create the observable subjects.
        this.goalOrdering = new SimpleSubject<>();
        this.orderedGoals = new SimpleSubject<>();
        this.date = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = new ArrayList<>(goals);

            orderedGoals.setValue(newOrderedGoals);
        });

        // When ordering changes, update the ordered goals
        goalOrdering.observe(ordering -> {
            if(ordering == null) return;

            var goals = new ArrayList<Goal>();
            for(var id : ordering) {
                var goal = goalRepository.find(id).getValue();
                if(goal == null) return;
                goals.add(goal);
            }
            this.orderedGoals.setValue(goals);
        });

        // When the current date changes, update our date
        dateRepository.getDate().observe(dateValue -> {
            if (dateValue == null) {
                return;
            }

            date.setValue(dateValue);
        });
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }

    /**
     * Append goal to the end of the list
     * @param goal goal to be appended
     */
    public void append(Goal goal) {
        goalRepository.append(goal);
    }

    // Prepend code from lab 5, wasn't working
    // public void prepend(Goal goal) { goalRepository.prepend(goal); }

    public void advanceDateOneDayForward() {
        dateRepository.advanceDateOneDayForward();
    }
  
    public void changeIsCompleteStatus(Integer id, Calendar dateCompleted) {
        goalRepository.changeIsCompleteStatus(id, dateCompleted);
    }

    public Subject<Calendar> getDate() {
        return date;
    }

    private LiveData<List<Goal>> convertSubjectToLiveData(Subject<List<Goal>> subject) {
        MutableLiveData<List<Goal>> liveData = new MutableLiveData<>();
        subject.observe(liveData::setValue);
        return liveData;
    }

    public LiveData<List<Goal>> getActiveGoals(Calendar mutableDate) {
        Subject<List<Goal>> activeGoalsSubject = goalRepository.getActiveGoalsSubject(getDate().getValue());
        return convertSubjectToLiveData(activeGoalsSubject);
//         return Transformations.map(activeGoals, goals -> {
//             // Get the current date with time set to midnight for comparison
//             Calendar currentDate = Calendar.getInstance();
//             currentDate.set(Calendar.HOUR_OF_DAY, 0);
//             currentDate.set(Calendar.MINUTE, 0);
//             currentDate.set(Calendar.SECOND, 0);
//             currentDate.set(Calendar.MILLISECOND, 0);

// //            // Filter out completed goals from previous days
// //            return goals.stream()
// //                    .filter(goal -> !goal.getIsComplete() || (goal.getDateCompleted() != null && goal.getDateCompleted().compareTo(currentDate) >= 0))
// //                    .collect(Collectors.toList());
//             // Log the current date for debugging
//             Log.d("getActiveGoals", "Current date (12 AM): " + currentDate.getTime());

//             // Filter out completed goals from previous days (before 2 AM)
//             List<Goal> filteredGoals = goals.stream()
//                 .filter(goal -> {
//                     boolean isGoalActive = !goal.getIsComplete() || (goal.getDateCompleted() != null && goal.getDateCompleted().compareTo(currentDate) >= 0);
//                     // Log each goal's status for debugging
//                     Log.d("getActiveGoals", "Goal: " + goal.getGoalText() + ", IsComplete: " + goal.getIsComplete() + ", DateCompleted: " + (goal.getDateCompleted() != null ? goal.getDateCompleted().getTime() : "null") + ", IsActive: " + isGoalActive);
//                     Log.d("getActiveGoals", "Current date: " + currentDate.getTime());
//                     Log.d("getActiveGoals", "Goal completion date: " + (goal.getDateCompleted() != null ? goal.getDateCompleted().getTime() : "null"));

//                     return isGoalActive;
//                 })
//                 .collect(Collectors.toList());

//             return filteredGoals;
//         });
    }
}
