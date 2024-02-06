package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;

import  edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;

    // UI state
    private final SimpleSubject<List<Integer>> cardOrdering;
    private final SimpleSubject<List<Goal>> orderedGoals;
    private final SimpleSubject<String> displayedText;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository());
                    });

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        // Create the observable subjects.
        this.cardOrdering = new SimpleSubject<>();
        this.displayedText = new SimpleSubject<>();
        this.orderedGoals = new SimpleSubject<>();

        // When the list of cards changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var ordering = new ArrayList<Integer>();
            for (int i = 0; i < cards.size(); i++) {
                ordering.add(i);
            }
            cardOrdering.setValue(ordering);
        });

        // when ordering changes, update the ordered cards
        cardOrdering.observe(ordering -> {
            if(ordering == null) return;

            var cards = new ArrayList<Goal>();
            for(var id : ordering) {
                var card = goalRepository.find(id).getValue();
                if(card == null) return;
                cards.add(card);
            }
            this.orderedGoals.setValue(cards);
        });

    }

    public SimpleSubject<String> getDisplayedText() {
        return displayedText;
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }
}
