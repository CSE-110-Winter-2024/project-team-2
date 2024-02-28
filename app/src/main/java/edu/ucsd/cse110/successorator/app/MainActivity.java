package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.changeView.ChangeViewFragment;
import edu.ucsd.cse110.successorator.app.ui.goalList.GoalListFragment;
import edu.ucsd.cse110.successorator.app.ui.goalList.dialog.AddGoalDialogFragment;
import edu.ucsd.cse110.successorator.app.ui.noGoals.NoGoalsFragment;
import edu.ucsd.cse110.successorator.lib.util.date.CurrentDateProvider;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;
import edu.ucsd.cse110.successorator.lib.util.views.ViewTitleFormatter;

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

        // Listen for changes to date to update the app bar and isDisplayed values of goals
        this.activityModel.getDate().observe(date -> {
            if (date == null) {
                return;
            }

            // Make a copy of the date so we don't change the original
            Calendar mutableDate = (Calendar) date.clone();
            // Set our mutable date 2 hours back
            mutableDate.add(Calendar.HOUR_OF_DAY, -2);
            // Advance our mutable date if we are on Tomorrow's view
            if (this.activityModel.getView().getValue() == ViewOptions.TOMORROW) {
                mutableDate.add(Calendar.DATE,1);
            }

            // Displays the title on the app bar
            if (getSupportActionBar() != null) {
                getSupportActionBar()
                        .setTitle(new ViewTitleFormatter()
                                .formatViewTitle(this.activityModel.getView().getValue(), mutableDate));
            }

            // Update isDisplayed value of all goals and update database
            activityModel.updateAllGoalsIsDisplayed();
        });

        // Listen for changes to view to update the action bar and isDisplayed values of goals
        this.activityModel.getView().observe(viewType -> {
            if (viewType == null) {
                return;
            }

            Calendar date = activityModel.getDate().getValue();
            if (date == null) {
                return;
            }
            // Make a copy of the date so we don't change the original
            Calendar mutableDate = (Calendar) date.clone();
            // Set our mutable date 2 hours back
            mutableDate.add(Calendar.HOUR_OF_DAY, -2);
            // Advance our mutable date if we are on Tomorrow's view
            if (viewType == ViewOptions.TOMORROW) {
                mutableDate.add(Calendar.DATE,1);
            }

            // Displays the title on the app bar
            if (getSupportActionBar() != null) {
                getSupportActionBar()
                        .setTitle(new ViewTitleFormatter()
                                .formatViewTitle(viewType, mutableDate));
            }

            // Update isDisplayed value of all goals and update database
            activityModel.updateAllGoalsIsDisplayed();
        });

        // Listen for changes to goals so we can update which fragment to show
        activityModel.getOrderedGoals().observe(goals -> {
            if (goals == null) return;

            /*
             * If there are no goals, we want to show NoGoalsFragment. If there 
             * is at least one goal, we want to show GoalListFragment. 
             * We use isShowingNoGoals to track whether we are currently showing 
             * NoGoalsFragment, and we only replace the fragment when the fragment
             * we should show doesn't match what we're already showing
             */

            if (goals.size() == 0) {
                if (!isShowingNoGoals) {
                    // Replace GoalsListFragment with NoGoalsFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (!fragmentManager.isDestroyed()) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.goals_container, NoGoalsFragment.newInstance())
                                .commit();
                    }
                }
                isShowingNoGoals = true;
            } else {
                if (isShowingNoGoals) {
                    // Replace NoGoalsFragment with GoalsListFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (!fragmentManager.isDestroyed()) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.goals_container, GoalListFragment.newInstance())
                                .commit();
                    }
                }
                isShowingNoGoals = false;
            }

            // Update isDisplayed value of all goals and update database
            activityModel.updateAllGoalsIsDisplayed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * When our activity is resumed (e.g. user swiped to another app and then back to our app,
         * update our current date.
         */
        activityModel.setDate(new CurrentDateProvider());
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

        if (itemId == R.id.add_bar_manu_change_view) {
            var changeViewFragment = ChangeViewFragment.newInstance();
            changeViewFragment.show(getSupportFragmentManager(), "ChangeViewFragment");
        }

        return super.onOptionsItemSelected(item);
    }
}
