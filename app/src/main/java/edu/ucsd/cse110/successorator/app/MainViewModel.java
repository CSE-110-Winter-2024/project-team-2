package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
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
            if(orderedGoals.getValue() != null) {
                for(Goal goal : orderedGoals.getValue()) {
                    if(goal != null && getDate().getValue() != null){
                        goal.updateIsDisplayed(getDate().getValue());
                    }
                }
            }
        });

        // When ordering changes, update the ordered goals
        goalOrdering.observe(ordering -> {
            if(ordering == null) return;

            var goals = new ArrayList<Goal>();
            for(var id : ordering) {
                var goal = goalRepository.find(id).getValue();
                if(goal == null) return;
                goals.add(goal);
                goal.updateIsDisplayed(date.getValue()); // update is displayed value when ordering changes? DELETE??
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
  
    public void changeIsCompleteStatus(Integer id) {
        goalRepository.changeIsCompleteStatus(id);
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        goalRepository.setDateCompleted(id, dateCompleted);
    }

    public void updateIsDisplayed(Integer id, boolean isDisplayed) {
        goalRepository.changeIsDisplayedStatus(id, isDisplayed);
    }

    public Subject<Calendar> getDate() {
        return date;
    }
}
