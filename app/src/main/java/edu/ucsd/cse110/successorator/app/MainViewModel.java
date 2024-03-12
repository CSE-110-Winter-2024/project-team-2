package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.FocusModeRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;
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
    private final FocusModeRepository focusModeRepository;

    // UI state
    private final SimpleSubject<List<Goal>> orderedGoals;
    private final SimpleSubject<List<Goal>> allGoals;
    private final SimpleSubject<Calendar> date;
    private final SimpleSubject<ViewOptions> view;
    private final SimpleSubject<GoalContext> focusContext;

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
                        return new MainViewModel(app.getGoalRepository(), app.getDateRepository(),
                                app.getViewRepository(), app.getFocusModeRepository());
                    });

    public MainViewModel(GoalRepository goalRepository, DateRepository dateRepository,
                         ViewRepository viewRepository, FocusModeRepository focusModeRepository) {
        this.goalRepository = goalRepository;
        this.dateRepository = dateRepository;
        this.viewRepository = viewRepository;
        this.focusModeRepository = focusModeRepository;

        // Create the observable subjects.
        this.orderedGoals = new SimpleSubject<>();
        this.allGoals = new SimpleSubject<>();
        this.date = new SimpleSubject<>();
        this.selectedGoalContextId = new SimpleSubject<>();
        this.longPressGoalId = new SimpleSubject<>();
        this.view = new SimpleSubject<>();
        this.focusContext = new SimpleSubject<>();

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

        // When the current focus mode changes, update our focusContext
        focusModeRepository.getFocusContext().observe(context -> {
            focusContext.setValue(context);
        });
    }

    public Goal rawFindGoal(int id) {
        return goalRepository.rawFind(id);
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

    public Subject<List<Goal>> findAllSortedByContext(){
        return goalRepository.findAllSortedByContext();
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
        goalRepository.moveFromPending(id, new MockDateProvider(getDate().getValue()).getCurrentViewDate(ViewOptions.TODAY));
    }

    public void moveToTomorrow(Integer id) {
        goalRepository.moveFromPending(id, new MockDateProvider(getDate().getValue()).getCurrentViewDate(ViewOptions.TOMORROW));
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
                goal.updateIsDisplayed(mutableDate, getView().getValue(), getFocusContext().getValue());

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
  
    public Subject<GoalContext> getFocusContext() {
        return focusContext;
    }

    public void setFocusContext(GoalContext context) {
        focusModeRepository.setFocusContext(context);
    }

    public void deleteGoal(int id) {
        goalRepository.deleteGoal(id);
    }

    public void deleteRecurringGoalTemplate(int id) {
        // Find all recurring goal instances of this template
        List<Goal> instancesOfTemplate = goalRepository.findGoalsByTemplateId(id);
        if (instancesOfTemplate != null) {
            // Loop over all instances of this template
            for (Goal instance : instancesOfTemplate) {
                // Step 1: set templateId to null for all instances so they don't make future instances
                goalRepository.setTemplateId(instance.getId(), null);

                // Step 2: delete any instances whose goalDate is AFTER tomorrow
                if (new DateComparer().compareDates(
                        new MockDateProvider(instance.getGoalDate()).getCurrentViewDate(ViewOptions.TODAY),
                        new MockDateProvider(getDate().getValue()).getCurrentViewDate(ViewOptions.TOMORROW)
                ) > 0) {
                    goalRepository.deleteGoal(instance.getId());
                }
            }
        }

        // Step 3: delete the recurring template itself
        deleteGoal(id);
    }
}
