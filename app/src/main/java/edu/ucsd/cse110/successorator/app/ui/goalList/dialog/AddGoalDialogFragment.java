package edu.ucsd.cse110.successorator.app.ui.goalList.dialog;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogAddGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

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
     *  construct new fragment instance
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

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Goal")
                .setMessage("Please enter your goal")
                .setView(view.getRoot())
                .setPositiveButton("", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .setPositiveButtonIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_check))
                .create();
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
        if(!goalText.equals("")){
            var goal = new Goal(null, goalText, -1, false);
            activityModel.append(goal);
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
