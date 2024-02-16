package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.FragmentGoalListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * This class displays the goal list in a Fragment view
 */
public class GoalListFragment  extends Fragment{
    private FragmentGoalListBinding binding;
    private MainViewModel activityModel;
    private GoalListAdapter adapter;

    /**
     * Required empty public constructor
     */
    public GoalListFragment () { }

    /**
     * construct new fragment instance
     *
     * @return GoalListFragment
     */
    public static GoalListFragment newInstance() {
        GoalListFragment fragment = new GoalListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On creation of this fragment gets the order of goals to display
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with empty list for now)
        this.adapter = new GoalListAdapter(requireContext(), new ArrayList<>(), id -> {
            activityModel.changeIsCompleteStatus(id);
        });

        //Observe active goals
        activityModel.getActiveGoals().observe(getViewLifecycleOwner(), goals -> {
            if (goals == null) return;
            adapter.setGoals(new ArrayList<>(goals)); //remember the mutable copy here
        });
    }*/



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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @NonNull ViewGroup container,
            @NonNull Bundle savedInstanceState
    ) {
        this.binding = FragmentGoalListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter
        adapter = new GoalListAdapter(requireContext(), new ArrayList<>(), id -> {
            activityModel.changeIsCompleteStatus(id, activityModel.getDate().getValue());
        });


        // Set the adapter on the ListView
        binding.goalList.setAdapter(adapter);

        // Observe active goals
        activityModel.getActiveGoals().observe(getViewLifecycleOwner(), goals -> {
            if (goals != null) {
                adapter.setGoals(new ArrayList<>(goals));
            }
        });

    }
}
