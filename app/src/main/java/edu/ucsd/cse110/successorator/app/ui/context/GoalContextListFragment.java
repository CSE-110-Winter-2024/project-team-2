package edu.ucsd.cse110.successorator.app.ui.context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentGoalContextListBinding;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;

/**
 * This class displays the list of available contexts, used when adding a goal and choosing its context.
 */
public class GoalContextListFragment extends Fragment {
    private FragmentGoalContextListBinding view;
    private MainViewModel activityModel;
    private GoalContextListAdapter adapter;

    /**
     * Required empty public constructor
     */
    public GoalContextListFragment() { }

    /**
     * construct new fragment instance
     *
     * @return ContextListFragment
     */
    public static GoalContextListFragment newInstance() {
        GoalContextListFragment fragment = new GoalContextListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On creation of this fragment gets the default goal contexts to display.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter with the default goal contexts, since the list of contexts will never change
        this.adapter = new GoalContextListAdapter(this, GoalContext.defaultGoalContexts, id -> {
            activityModel.setSelectedGoalContextId(id);
        });
    }

    /**
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
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        this.view = FragmentGoalContextListBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        view.goalContextList.setLayoutManager(layoutManager);

        // Set adapter on the RecyclerView
        view.goalContextList.setAdapter(adapter);
        return view.getRoot();
    }
}
