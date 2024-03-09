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
import edu.ucsd.cse110.successorator.lib.domain.Goal.RecurType;
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

    /*
     * The ID of the goal that we are currently taking an action on with the long-press menu.
     * Null if we are not long-pressing any goal
     */
    private final SimpleSubject<Integer> longPressGoalId;

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
        this.longPressGoalId = new SimpleSubject<>();
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
            this.updateAllGoalsIsDisplayed();
        });

        // When the current date changes, update our date
        dateRepository.getDate().observe(dateValue -> {
            if (dateValue == null) return; // not ready yet, ignore

            date.setValue(dateValue);
            this.updateAllGoalsIsDisplayed();
        });

        // When the current view changes, update our view
        viewRepository.getView().observe(viewType -> {
            if (viewType == null) return; // not ready yet, ignore

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
     * @return the ID of the appended goal
     */
    public int append(Goal goal) {
        return goalRepository.append(goal);
    }

    public void advanceDateOneDayForward() {
        dateRepository.advanceDateOneDayForward();
    }

    public void setDate(DateProvider dateProvider) {
        dateRepository.setDate(dateProvider);
    }

    public void changeIsCompleteStatus(Integer id) {
        goalRepository.changeIsCompleteStatus(id);
        if (goalRepository.getIsPendingStatus(id)) {
            moveToToday(id);
        }
        moveToTop(id);
        setDateCompleted(id, getDate().getValue());
    }

    public void moveToToday(Integer id) {
        goalRepository.changeIsPendingStatus(id, false);
        goalRepository.setGoalDate(id, new MockDateProvider(getDate().getValue()).getCurrentViewDate(ViewOptions.TODAY));
    }

    public void moveToTomorrow(Integer id) {
        goalRepository.changeIsPendingStatus(id, false);
        goalRepository.setGoalDate(id, new MockDateProvider(getDate().getValue()).getCurrentViewDate(ViewOptions.TOMORROW));
    }

    public void moveToTop(Integer id) {
        goalRepository.moveToTop(id);
    }

    public void setDateCompleted(Integer id, Calendar dateCompleted) {
        goalRepository.setDateCompleted(id, dateCompleted);
    }

    public void updateAllGoalsIsDisplayed() {
        if (getAllGoals().getValue() != null && getDate().getValue() != null) {
            Calendar date = (Calendar) getDate().getValue().clone();
            date.add(Calendar.DATE, 1);

            for (Goal goal : getAllGoals().getValue()) {
                /*
                 * Iterate through all goals and updated isDisplayed value based on
                 * current view and system date, and update database
                 */

                // Step 1: Check whether we should make a recurrence for this goal
                Goal templateGoal = goal.getTemplateId() == null ? null : goalRepository.rawFind(goal.getTemplateId());
                boolean shouldMakeNextOccurrence = goal.recurType == Goal.RecurType.RECURRING_INSTANCE
                        && getDate().getValue() != null && new DateComparer().compareDates(goal.getGoalDate(), date) < 0
                        && templateGoal != null
                        && goal.getNextRecurrence() == null;

                // Step 2: Update next recurrence date of this goal, if necessary
                if (shouldMakeNextOccurrence) {
                    goal.updateNextRecurrence(templateGoal);
                }

                // Step 3: Update isDisplayed for this goal
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

                // Step 4: Create next recurring goal in DB
                if (shouldMakeNextOccurrence) {
                    Goal nextGoalRecurrence = goal.makeNextOccurrence(templateGoal);

                    if (nextGoalRecurrence != null) {
                        goalRepository.setNextRecurrence(goal.getId(), nextGoalRecurrence.getGoalDate());
                        append(nextGoalRecurrence);
                    }
                }
            }
        }
    }

    public boolean hasActivePrevGoal(Goal goal) {
        if (getAllGoals().getValue() != null) {
            for (Goal pastGoal : getAllGoals().getValue()) {
                if (Objects.equals(pastGoal.getId(), goal.getPastRecurrenceId())) {
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

    public Subject<Integer> getLongPressGoalId() {
        return longPressGoalId;
    }

    public void setLongPressGoalId(Integer goalId) {
        longPressGoalId.setValue(goalId);
    }
    
    public void setView(ViewOptions view) {
        viewRepository.setView(view);
    }

    public Subject<ViewOptions> getView() {
        return view;
    }
}
