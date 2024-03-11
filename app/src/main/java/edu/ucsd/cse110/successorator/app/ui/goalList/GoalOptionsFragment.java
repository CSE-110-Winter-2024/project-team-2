package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentGoalOptionsBinding;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

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

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        // Set up click listeners for when different actions are pressed
        view.todayAction.setOnClickListener(view -> {
            activityModel.moveToToday(activityModel.getLongPressGoalId().getValue());

            activityModel.setLongPressGoalId(null);
            dialog.dismiss();
        });
        view.tomorrowAction.setOnClickListener(view -> {
            activityModel.moveToTomorrow(activityModel.getLongPressGoalId().getValue());

            activityModel.setLongPressGoalId(null);
            dialog.dismiss();
        });
        view.finishAction.setOnClickListener(view -> {
            activityModel.changeIsCompleteStatus(activityModel.getLongPressGoalId().getValue());

            activityModel.setLongPressGoalId(null);
            dialog.dismiss();
        });

        view.deleteAction.setOnClickListener(view -> {
            activityModel.deleteGoal(activityModel.getLongPressGoalId().getValue());

            activityModel.setLongPressGoalId(null);
            dialog.dismiss();
        });

        return dialog;
    }
}
