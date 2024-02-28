package edu.ucsd.cse110.successorator.app.ui.changeView;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentChangeViewBinding;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class ChangeViewFragment extends DialogFragment {
    private FragmentChangeViewBinding view;
    private MainViewModel activityModel;

    /**
     * Required empty constructor
     */
    public ChangeViewFragment() { }

    /**
     * Construct new fragment instance
     * @return fragment
     */
    public static ChangeViewFragment newInstance() {
        ChangeViewFragment fragment = new ChangeViewFragment();
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
         this.view = FragmentChangeViewBinding.inflate(getLayoutInflater());

         final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                 .setView(view.getRoot())
                 .create();

         // Set up click listeners for when different view options are pressed
         view.todayViewOption.setOnClickListener(view -> {
             activityModel.setView(ViewOptions.TODAY);
             dialog.dismiss();
         });
         view.tomorrowViewOption.setOnClickListener(view -> {
             activityModel.setView(ViewOptions.TOMORROW);
             dialog.dismiss();
         });
         view.pendingViewOption.setOnClickListener(view -> {
             activityModel.setView(ViewOptions.PENDING);
             dialog.dismiss();
         });
         view.recurringViewOption.setOnClickListener(view -> {
             activityModel.setView(ViewOptions.RECURRING);
             dialog.dismiss();
         });

         return dialog;
     }
}
