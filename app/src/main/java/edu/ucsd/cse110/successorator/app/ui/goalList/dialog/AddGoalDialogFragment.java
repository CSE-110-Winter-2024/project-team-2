package edu.ucsd.cse110.successorator.app.ui.goalList.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.FragmentDialogAddGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;
import edu.ucsd.cse110.successorator.lib.domain.GoalFactory;
import edu.ucsd.cse110.successorator.lib.util.date.DateComparer;
import edu.ucsd.cse110.successorator.lib.util.date.DateFormatter;
import edu.ucsd.cse110.successorator.lib.util.date.MockDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * This class maintains the Alert Dialog that allows the user to add a new goal to the list
 */
public class AddGoalDialogFragment extends DialogFragment {
    private FragmentDialogAddGoalBinding view;
    private MainViewModel activityModel;

    /**
     * Required empty constructor
     */
    AddGoalDialogFragment() { }

    /**
     * Construct new fragment instance
     * @return fragment
     */
    public static AddGoalDialogFragment newInstance() {
        var fragment = new AddGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create of Alert Dialog, asks user for new goal input
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        activityModel.setSelectedGoalContextId(null);
    }

    /**
     * On creation of dialog set Alert Dialog values: title, message, buttons
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogAddGoalBinding.inflate(getLayoutInflater());
        ViewOptions currentView = activityModel.getView().getValue();
        Calendar currDate = new MockDateProvider(activityModel.getDate().getValue()).getCurrentViewDate(currentView);
        updateStartDateText(currDate);
        view.datePicker.setMinDate(currDate.getTimeInMillis());

        if (currentView == ViewOptions.RECURRING) {
            view.oneTimeButton.setVisibility(View.GONE);
            view.weeklyButton.setChecked(true);
            view.startingLabel.setVisibility(View.VISIBLE);
            view.startDateButton.setVisibility(View.VISIBLE);

            updateStartDateText(currDate);

            view.startDateButton.setOnClickListener(v -> toggleDatePickerVisibility());

            view.setDateButton.setOnClickListener(v -> {
                Calendar datePicked = Calendar.getInstance();
                datePicked.set(Calendar.MONTH, view.datePicker.getMonth());
                datePicked.set(Calendar.YEAR, view.datePicker.getYear());
                datePicked.set(Calendar.DAY_OF_MONTH, view.datePicker.getDayOfMonth());
                // Set date past 2 AM to ensure it doesn't get counted as previous day
                datePicked.set(Calendar.HOUR, 5);
                if (new DateComparer().compareDates(datePicked, activityModel.getDate().getValue()) >= 0) {
                    toggleDatePickerVisibility();
                    updateStartDateText(datePicked);
                }
            });
        } else if (currentView == ViewOptions.PENDING) {
            view.radioGroup.setVisibility(View.GONE);
        }

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("New Goal")
                .setMessage("Please enter your goal")
                .setView(view.getRoot())
                .setPositiveButton("", null)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .setPositiveButtonIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_check))
                .create();

        /*
         * Once the dialog is shown, we can grab the positive button and set its
         * click listener to override the default behavior of dismissing the dialog.
         * This way, we can prevent the dialog from being dismissed if the user
         * didn't enter any goal text.
         */
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setOnClickListener(view -> {
                    onPositiveButtonClick(dialog, AlertDialog.BUTTON_POSITIVE);
                });
            }
        });

        return dialog;
    }

    private void updateStartDateText(Calendar date) {
        String formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date.getTime());

        this.view.startDateButton.setText(formattedDate);
        this.view.weeklyButton.setText(String.format("Weekly on %s", new DateFormatter().formatWeekDay(date)));
        this.view.monthlyButton.setText(String.format("Monthly on %s", new DateFormatter().formatDayOfMonth(date)));
        this.view.yearlyButton.setText(String.format("Yearly on %s", new DateFormatter().formatDayOfYear(date)));
    }

    private void toggleDatePickerVisibility() {
        if (view.datePicker.getVisibility() == View.GONE) {
            view.datePicker.setVisibility(View.VISIBLE);
            view.setDateButton.setVisibility(View.VISIBLE);
            view.startDateButton.setVisibility(View.GONE);
            view.radioGroup.setVisibility(View.GONE);
            view.contextListContainer.setVisibility(View.GONE);
        } else {
            view.datePicker.setVisibility(View.GONE);
            view.setDateButton.setVisibility(View.GONE);
            view.startDateButton.setVisibility(View.VISIBLE);
            view.radioGroup.setVisibility(View.VISIBLE);
            view.contextListContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * When the positive button is clicked get text from input,
     * create new goal, add goal to list
     *
     * @param dialog dialog
     * @param which which button
     */
    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var goalText = view.goalEditText.getText().toString();
        var selectedContextId = activityModel.getSelectedGoalContextId().getValue();
        // Don't allow user to create a goal if they haven't entered any text or selected a context
        if (!goalText.equals("") && selectedContextId != null) {
            Calendar goalDate = null;
            ViewOptions view = activityModel.getView().getValue();
            Goal.RecurrencePattern recurrencePattern = Goal.RecurrencePattern.NONE;
            Goal.RecurType recurType = Goal.RecurType.RECURRING_INSTANCE;

            if (view == ViewOptions.TODAY || view == ViewOptions.TOMORROW) {
                goalDate = new MockDateProvider(activityModel.getDate().getValue())
                        .getCurrentViewDate(view);
                if (this.view.oneTimeButton.isChecked()){
                    recurType = Goal.RecurType.NOT_RECURRING;
                }
                if (this.view.dailyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.DAILY;
                } else if (this.view.weeklyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.WEEKLY;
                } else if (this.view.monthlyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.MONTHLY;
                } else if (this.view.yearlyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.YEARLY;
                }
            } else if (view == ViewOptions.RECURRING) {
                goalDate = Calendar.getInstance();
                // Set date past 2 AM to ensure it doesn't get counted as previous day
                goalDate.set(Calendar.HOUR, 5);
                goalDate.set(this.view.datePicker.getYear(), this.view.datePicker.getMonth(), this.view.datePicker.getDayOfMonth());
                updateStartDateText(goalDate);

                if (this.view.dailyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.DAILY;
                } else if (this.view.weeklyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.WEEKLY;
                } else if (this.view.monthlyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.MONTHLY;
                } else if (this.view.yearlyButton.isChecked()) {
                    recurrencePattern = Goal.RecurrencePattern.YEARLY;
                }
            } else if (view == ViewOptions.PENDING){
                recurType = Goal.RecurType.NOT_RECURRING;
            }

            GoalFactory goalFactory = new GoalFactory();
            if(recurType == Goal.RecurType.RECURRING_INSTANCE) {
                // add goal for recur view
                var recurTemplate = goalFactory.makeRecurringTemplate(goalText, goalDate, view, selectedContextId, recurrencePattern);
                int recurGoalId = activityModel.append(recurTemplate);

                // add first goal occurrence, using ID of recurring template goal as templateId
                var recurInstance = goalFactory.makeRecurringInstance(goalText, goalDate, selectedContextId, recurrencePattern, recurGoalId);
                activityModel.append(recurInstance);

            } else {
                var goal = goalFactory.makeOneTimeGoal(goalText, goalDate, view, selectedContextId);
                activityModel.append(goal);
            }

            // Reset selected context ID to null so that no context will be selected by default next time
            activityModel.setSelectedGoalContextId(null);
            dialog.dismiss();
        }
    }

    /**
     * When the negative button is clicked back out of Alert dialog
     *
     * @param dialog dialog
     * @param which button
     */
    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}