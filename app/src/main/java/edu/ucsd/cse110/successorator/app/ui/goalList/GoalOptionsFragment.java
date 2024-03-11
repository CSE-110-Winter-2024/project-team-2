package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentGoalOptionsBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class GoalOptionsFragment extends DialogFragment {
    private FragmentGoalOptionsBinding view;
    private MainViewModel activityModel;

    /**
     * Required empty constructor
     */
    public GoalOptionsFragment() { }

    /**
     * Construct new fragment instance
     * @return fragment
     */
    public static GoalOptionsFragment newInstance() {
        GoalOptionsFragment fragment = new GoalOptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On creation, initialize the model
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
    }

    /**
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentGoalOptionsBinding.inflate(getLayoutInflater());

        int longPressGoalId = activityModel.getLongPressGoalId().getValue();
        Goal longPressedGoal = activityModel.rawFindGoal(longPressGoalId);

        boolean isPending = longPressedGoal.getIsPending();

        // Hide today, tomorrow, and finish options if the goal is recurring rather than pending
        int visibility = isPending ? View.VISIBLE : View.GONE;
        view.todayAction.setVisibility(visibility);
        view.tomorrowAction.setVisibility(visibility);
        view.finishAction.setVisibility(visibility);
        view.todayTomorrowBorder.setVisibility(visibility);
        view.tomorrowFinishBorder.setVisibility(visibility);
        view.finishDeleteBorder.setVisibility(visibility);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        // Set up click listeners for when different actions are pressed
        if (isPending) {
            view.todayAction.setOnClickListener(view -> {
                activityModel.moveToToday(longPressGoalId);

                activityModel.setLongPressGoalId(null);
                dialog.dismiss();
            });
            view.tomorrowAction.setOnClickListener(view -> {
                activityModel.moveToTomorrow(longPressGoalId);

                activityModel.setLongPressGoalId(null);
                dialog.dismiss();
            });
            view.finishAction.setOnClickListener(view -> {
                activityModel.changeIsCompleteStatus(longPressGoalId);

                activityModel.setLongPressGoalId(null);
                dialog.dismiss();
            });
        }

        view.deleteAction.setOnClickListener(view -> {
            if (isPending) {
                activityModel.deleteGoal(longPressGoalId);
            } else {
                activityModel.deleteRecurringGoalTemplate(longPressGoalId);
            }

            activityModel.setLongPressGoalId(null);
            dialog.dismiss();
        });

        return dialog;
    }
}
