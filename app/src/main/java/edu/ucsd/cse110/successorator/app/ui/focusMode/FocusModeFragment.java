package edu.ucsd.cse110.successorator.app.ui.focusMode;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentFocusModeBinding;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;

public class FocusModeFragment extends DialogFragment {
    private FragmentFocusModeBinding view;
    private MainViewModel activityModel;

    /**
     * Required empty constructor
     */
    public FocusModeFragment() { }

    /**
     * Construct new fragment instance
     * @return fragment
     */
    public static FocusModeFragment newInstance() {
        FocusModeFragment fragment = new FocusModeFragment();
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
        this.view = FragmentFocusModeBinding.inflate(getLayoutInflater());

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        // Set up click listeners for when different view options are pressed
        view.homeContext.setOnClickListener(context -> {
            activityModel.setFocusContext(GoalContext.defaultGoalContexts.get(0));
            dialog.dismiss();
        });
        view.workContext.setOnClickListener(context -> {
            activityModel.setFocusContext(GoalContext.defaultGoalContexts.get(1));
            dialog.dismiss();
        });
        view.schoolContext.setOnClickListener(context -> {
            activityModel.setFocusContext(GoalContext.defaultGoalContexts.get(2));
            dialog.dismiss();
        });
        view.errandsContext.setOnClickListener(context -> {
            activityModel.setFocusContext(GoalContext.defaultGoalContexts.get(3));
            dialog.dismiss();
        });
//        view.cancelContext.setOnClickListener(context -> {
//            activityModel.setFocusContext(null);
//            dialog.dismiss();
//        });

        return dialog;
    }
}
