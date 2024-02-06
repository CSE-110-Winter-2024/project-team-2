package edu.ucsd.cse110.successorator.ui.goalList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentGoalListBinding;

public class GoalListFragment  extends Fragment{
    private FragmentGoalListBinding view;
    private MainViewModel activityModel;
    private CardListAdapter adapter;

    public GoalListFragment () {
        // Required empty public constructor
    }

    public static GoalListFragment newInstance() {
        GoalListFragment fragment = new GoalListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        //Initialize the Adapter (with empty list for now)
        this.adapter = new CardListAdapter(requireContext(), List.of());
        activityModel.getOrderedGoals().observe(cards -> {
            if (cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards)); //remember the mutable copy here
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @NonNull ViewGroup container,
            @NonNull Bundle savedInstanceState
    ) {
        this.view = FragmentGoalListBinding.inflate(inflater, container, false);

        // set adapter on the listView
        view.goalList.setAdapter(adapter);

        return view.getRoot();
    }

}
