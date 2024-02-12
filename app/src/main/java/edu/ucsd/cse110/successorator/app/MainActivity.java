package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.goalList.GoalListFragment;
import edu.ucsd.cse110.successorator.app.ui.goalList.dialog.AddGoalDialogFragment;
import edu.ucsd.cse110.successorator.app.ui.noGoals.NoGoalsFragment;

/**
 * The main activity sets up the initial screen and triggers the Alert Dialog when user taps +.
 * It also handles switching between showing NoGoalsFragment and GoalListFragment, depending
 * on whether there are any goals to display.
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MainViewModel activityModel;
    private boolean isShowingNoGoals = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        // Initialize Model
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Listen for changes to goals so we can update which fragment to show
        activityModel.getOrderedGoals().observe(goals -> {
            if (goals == null) return;

            /**
             * If there are no goals, we want to show NoGoalsFragment. If there 
             * is at least one goal, we want to show GoalListFragment. 
             * We use isShowingNoGoals to track whether we are currently showing 
             * NoGoalsFragment, and we only replace the fragment when the fragment
             * we should show doesn't match what we're already showing
             */

            if (goals.size() == 0) {
                if (!isShowingNoGoals) {
                    // Replace GoalsListFragment with NoGoalsFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.goals_container, NoGoalsFragment.newInstance())
                            .commit();
                }
                isShowingNoGoals = true;
            } else {
                if (isShowingNoGoals) {
                    // Replace NoGoalsFragment with GoalsListFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.goals_container, GoalListFragment.newInstance())
                            .commit();
                }
                isShowingNoGoals = false;
            }
        });
    }

    /**
     * Set up options menu to include + icon in right corner
     *
     * @param menu The options menu in which you place your items.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_bar, menu);
        return true;
    }

    /**
     * When the user taps the + icon in the right, trigger
     * AlertDialog to allow user to enter new goal
     *
     * @param item The menu item that was selected.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.add_bar_manu_swap_views) {
            var dialogFragment = AddGoalDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "AddGoalDialogFragment");
        }

        return super.onOptionsItemSelected(item);
    }
}
