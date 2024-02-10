package edu.ucsd.cse110.successorator.app.ui.noGoals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.FragmentNoGoalsBinding;

/**
 * This class is currently not in use, but will display the defualt no goals text on the screen as a fragment
 */
public class NoGoalsFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentNoGoalsBinding view;

    /**
     * Required empty public constructor
     */
    public NoGoalsFragment() { }

    /**
     * creates new fragment
     * @return noGoalsFragment
     */
    public static NoGoalsFragment newInstance() {
        NoGoalsFragment fragment = new NoGoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On creation initialize the model
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = FragmentNoGoalsBinding.inflate(inflater,container,false);
        view.placeholderText.setText(R.string.noGoalsText);
        setupMvp();
        return view.getRoot();
    }

    public void setupMvp() {

    }
}