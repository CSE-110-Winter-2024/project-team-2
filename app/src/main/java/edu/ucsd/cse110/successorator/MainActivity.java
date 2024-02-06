package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.goalList.GoalListFragment;
import edu.ucsd.cse110.successorator.ui.noGoals.noGoalsFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private boolean isShowingGoalList = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.add_bar_manu_swap_views) {
            swapFragments();
        }

        return super.onOptionsItemSelected(item);
    }

    private void swapFragments() {
        if(!isShowingGoalList) { //change to based on if there are goals in the list later
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, noGoalsFragment.newInstance())
                    .commit();

        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, GoalListFragment.newInstance())
                    .commit();
        }
        isShowingGoalList = !isShowingGoalList;
    }
}
