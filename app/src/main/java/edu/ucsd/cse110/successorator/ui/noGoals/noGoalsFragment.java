package edu.ucsd.cse110.successorator.ui.noGoals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentNoGoalsBinding;


public class noGoalsFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentNoGoalsBinding view;

    public noGoalsFragment() {
        // Required empty public constructor
    }

    public static noGoalsFragment newInstance() {
        noGoalsFragment fragment = new noGoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

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
        //Observe Model -> call view
        //activityModel.getDisplayedText().observe(text -> view.cardText.setText(text));
    }
}