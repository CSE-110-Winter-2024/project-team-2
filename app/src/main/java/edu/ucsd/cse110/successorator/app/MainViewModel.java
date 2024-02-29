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
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.date.DateProvider;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;
    private final DateRepository dateRepository;
    private final ViewRepository viewRepository;

    // UI state
    private final SimpleSubject<List<Integer>> goalOrdering;
    private final SimpleSubject<List<Goal>> orderedGoals;
    private final SimpleSubject<List<Goal>> allGoals;
    private final SimpleSubject<Calendar> date;
    private final SimpleSubject<ViewOptions> view;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository(), app.getDateRepository(), app.getViewRepository());
                    });

    public MainViewModel(GoalRepository goalRepository, DateRepository dateRepository, ViewRepository viewRepository) {
        this.goalRepository = goalRepository;
        this.dateRepository = dateRepository;
        this.viewRepository = viewRepository;

        // Create the observable subjects.
        this.goalOrdering = new SimpleSubject<>();
        this.orderedGoals = new SimpleSubject<>();
        this.allGoals = new SimpleSubject<>();
        this.date = new SimpleSubject<>();
        this.view = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = new ArrayList<>(goals);

            orderedGoals.setValue(newOrderedGoals);
            if (orderedGoals.getValue() != null) {
                for (Goal goal : orderedGoals.getValue()) {
                    if (goal != null && getDate().getValue() != null){
                        Calendar mutableDate = new MockDateProvider(getDate().getValue())
                                .getCurrentViewDate(getView().getValue());
                        goal.updateIsDisplayed(mutableDate, getView().getValue());
                    }
                }
            }
        });

        goalRepository.getAllGoals().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newAllGoals = new ArrayList<>(goals);

            allGoals.setValue(newAllGoals);
            if (allGoals.getValue() != null) {
                for (Goal goal : allGoals.getValue()) {
                    if (goal != null && getDate().getValue() != null){
                        Calendar mutableDate = new MockDateProvider(getDate().getValue())
                                .getCurrentViewDate(getView().getValue());
                        goal.updateIsDisplayed(mutableDate, getView().getValue());
                    }
                }
            }
        });

        // When ordering changes, update the ordered goals
        goalOrdering.observe(ordering -> {
            if (ordering == null) return;

            var goals = new ArrayList<Goal>();
            for (var id : ordering) {
                var goal = goalRepository.find(id).getValue();
                if (goal == null) return;
                goals.add(goal);
            }
            this.orderedGoals.setValue(goals);
        });

        // When there's a change to all goals, update whether or not they are displayed
        allGoals.observe(goals -> {
            if (goals == null) return;
            this.updateAllGoalsIsDisplayed();
        });

        // When the current date changes, update our date
        dateRepository.getDate().observe(dateValue -> {
            if (dateValue == null) {
                return;
            }

            date.setValue(dateValue);
        });

        // When the current view changes, update our view
        viewRepository.getView().observe(viewType -> {
            if (viewType == null) {
                return;
            }

            view.setValue(viewType);
        });
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }

    public Subject<List<Goal>> getAllGoals() {
        return allGoals;
    }

    /**
     * Append goal to the end of the list
     * @param goal goal to be appended
     */
    public void append(Goal goal) {
        goalRepository.append(goal);
    }

    public void advanceDateOneDayForward() {
        dateRepository.advanceDateOneDayForward();
    }

    public void setDate(DateProvider dateProvider) {
        dateRepository.setDate(dateProvider);
    }

    public void changeIsCompleteStatus(Integer id) {
        goalRepository.changeIsCompleteStatus(id);
    }

    public void moveToTop(Integer id) {
        goalRepository.moveToTop(id);
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        goalRepository.setDateCompleted(id, dateCompleted);
    }

    public void updateAllGoalsIsDisplayed() {
        if (getAllGoals().getValue() != null) {
            for (Goal goal : getAllGoals().getValue()) {
                /*
                 * Iterate through all goals and updated isDisplayed value based on
                 * current view and system date, and update database
                 */
                if (getDate().getValue() != null) {
                    boolean wasDisplayed = goal.getIsDisplayed();
                    Calendar mutableDate = new MockDateProvider(getDate().getValue())
                            .getCurrentViewDate(getView().getValue());
                    goal.updateIsDisplayed(mutableDate, getView().getValue());

                    /*
                     * Only propagate change to database if isDisplayed actually changed, to avoid
                     * infinite observer & updating loop
                     */
                    if (wasDisplayed != goal.getIsDisplayed()) {
                        goalRepository.changeIsDisplayedStatus(goal.getId(), goal.getIsDisplayed());
                    }
                }
            }
        }
    }

    public Subject<Calendar> getDate() {
        return date;
    }

    public void setView(ViewOptions view) {
        viewRepository.setView(view);
    }

    public Subject<ViewOptions> getView() {
        return view;
    }
}
