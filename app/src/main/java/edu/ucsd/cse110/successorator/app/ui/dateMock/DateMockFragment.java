package edu.ucsd.cse110.successorator.app.ui.dateMock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.FragmentDateMockBinding;

/**
 * This class displays a button that can be used to advance the date 1 day forward
 */
public class DateMockFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentDateMockBinding view;

    /**
     * Required empty public constructor
     */
    public DateMockFragment() { }

    /**
     * creates new fragment
     * @return DateMockFragment
     */
    public static DateMockFragment newInstance() {
        DateMockFragment fragment = new DateMockFragment();
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
        view = FragmentDateMockBinding.inflate(inflater,container,false);

        // Set up click listener for when advance date button is pressed
        view.dateMockButton.setOnClickListener(view -> {
            // TODO: advance date 1 day forward in data model
        });

        return view.getRoot();
    }
}