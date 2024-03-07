package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.ViewRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;
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

    /*
     The ID of the currently selected goal context when the user is adding a goal and choosing a context.
     Null if they have not chosen a context or are not currently adding a goal.
     */
    private final SimpleSubject<Integer> selectedGoalContextId;

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
        this.selectedGoalContextId = new SimpleSubject<>();
        this.view = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = new ArrayList<>(goals);

            orderedGoals.setValue(newOrderedGoals);
        });

        goalRepository.getAllGoals().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newAllGoals = new ArrayList<>(goals);

            allGoals.setValue(newAllGoals);
        });

        // When there's a change to all goals, update whether or not they are displayed
        allGoals.observe(goals -> {
            if (goals == null) return;
           this.addRecurringGoals(this);
           this.updateAllGoalsIsDisplayed();
        });

        // When the current date changes, update our date
        dateRepository.getDate().observe(dateValue -> {
            if (dateValue == null) {
                return;
            }

            date.setValue(dateValue);
            this.addRecurringGoals(this);
            this.updateAllGoalsIsDisplayed();
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

    public void changeIsCompleteStatus(Integer id, Calendar date) {
        goalRepository.changeIsCompleteStatus(id, date);
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
                    goal.updateIsDisplayed(mutableDate,getView().getValue());
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

    public void addRecurringGoals(MainViewModel activityModel) {
        if (getAllGoals().getValue() != null && getDate().getValue() != null) {
            Calendar date = (Calendar) getDate().getValue().clone();
            date.add(Calendar.DATE, 1);

            for (Goal goal : goalRepository.queryAllGoals()) {
                if (goal.recurType == 2) {
                    /*
                     * Iterate through all goals and updated nextRecurrence value based on
                     * current view and system date, and update database
                     */

                    if (getDate().getValue() != null && new DateComparer().compareDates(goal.getGoalDate(), date) < 0) {
                        Calendar mutableDate = new MockDateProvider(getDate().getValue())
                                .getCurrentViewDate(getView().getValue());
                        goal.updateIsDisplayed(mutableDate, getView().getValue());
                        Goal nextGoalRecurrence = goal.makeNextOccurrence();

                        if (nextGoalRecurrence != null) {
                            nextGoalRecurrence.updateIsDisplayed(mutableDate,getView().getValue());
                            goalRepository.setNextRecurrence(goal.getId(), nextGoalRecurrence.getGoalDate());
                            activityModel.append(nextGoalRecurrence);
                        }
                    }
                }
            }
        }
    }

    public boolean hasActivePrevGoal(Goal goal) {
        if (getAllGoals().getValue() != null) {
            for (Goal pastGoal : getAllGoals().getValue()) {
                if(Objects.equals(pastGoal.getId(), goal.getPastRecurrenceId())){
                    return !pastGoal.getIsComplete();
                }
            }
        }
        return false;
    }

    public Subject<Calendar> getDate() {
        return date;
    }

    public Subject<Integer> getSelectedGoalContextId() {
        return selectedGoalContextId;
    }

    public void setSelectedGoalContextId(Integer contextId) {
        selectedGoalContextId.setValue(contextId);
    }
    
    public void setView(ViewOptions view) {
        viewRepository.setView(view);
    }

    public Subject<ViewOptions> getView() {
        return view;
    }
}
