package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.GoalListItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.Goal.RecurType;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

/**
 * This class maintains the list of goals
 */
public class GoalListAdapter extends ArrayAdapter<Goal> {
    FragmentActivity activity;
    Consumer<Integer> onClick;
    MainViewModel activityModel;

    public GoalListAdapter(FragmentActivity activity, List<Goal> goals, Consumer<Integer> onClick, MainViewModel activityModel) {
       /*
        * This sets a bunch of stuff internally, which we can access
        * with getContext() and getItem() for example.
        *
        * Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        * or it will crash!
        */
        super(activity, 0, new ArrayList<>(goals));
        this.activity = activity;
        this.onClick = onClick;
        this.activityModel = activityModel;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the goal for this position.
        var goal = getItem(position);
        assert goal != null;

        // Check if a view is being reused...
        GoalListItemBinding binding;
        if (convertView != null) {
            // If so, bind to it
            binding = GoalListItemBinding.bind(convertView);
        } else {
            // Otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = GoalListItemBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the goal's data.
        binding.goalTextView.setText(goal.getGoalText());

        // Clear strikethrough in case this goal was recycled from a strikethroughed goal
        binding.goalTextView.setPaintFlags(binding.goalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        // Display as strikethrough if goal isn't pending and isComplete is true
        // if (!goal.getIsPending() && goal.getIsComplete()) {
        if (goal.getRecurType() != Goal.RecurType.RECURRING_TEMPLATE) {
            if (goal.getIsComplete()) { // Delete this later for US12 (Move Goals Between Views)
                binding.goalTextView.setPaintFlags(binding.goalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.goalTextView.setPaintFlags(binding.goalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        // Bind the goal text view to the callback.
        binding.goalTextView.setOnClickListener(v -> {
            // For pending goals, don't do anything on single tap
            if (goal.getIsPending()) {
                return;
            }

            var id = goal.getId();

            if (ViewOptions.TOMORROW == activityModel.getView().getValue() && activityModel.hasActivePrevGoal(goal)) {
                // Show a dialog box with the message
                Toast.makeText(getContext(), "This goal is still active for Today. Mark it finished in the Today view.", Toast.LENGTH_LONG).show();
                return;
            }

            onClick.accept(id);
        });

        binding.goalTextView.setOnLongClickListener(v -> {
            // Only display long-press options for pending goals and recurring TEMPLATES (NOT recurring instances)
            boolean showOptionsMenu = goal.getIsPending() || goal.getRecurType() == RecurType.RECURRING_TEMPLATE;
            if (!showOptionsMenu) {
                return false;
            }

            activityModel.setLongPressGoalId(goal.getId());
            var goalOptionsFragment = GoalOptionsFragment.newInstance();
            goalOptionsFragment.show(activity.getSupportFragmentManager(), "GoalOptionsFragment");

            return true;
        });

        // Set the text and background color of the goal context circle based on the context
        var goalContext = goal.getGoalContext();

        Button goalContextButton = binding.goalContextButton;
        goalContextButton.setText(Character.toString(goalContext.getFirstLetterOfName()));

        // Always set goal context color to full brightness for list display
        int goalContextColor = Color.parseColor(goalContext.getColor());
        goalContextButton.getBackground().setColorFilter(goalContextColor, PorterDuff.Mode.MULTIPLY );

        return binding.getRoot();
    }

    /*
     * The below methods aren't strictly necessary, usually.
     * But get in the habit of defining them because they never hurt
     * (as long as you have IDs for each item) and sometimes you need them.
     */

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void addAll(List<Goal> goals) {
        // Add all goals to the adapter
        for (Goal goal : goals) {
            add(goal);
        }
        // Notify the adapter that the data set has changed
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        var goal = getItem(position);
        assert goal != null;

        var id = goal.getId();
        assert id != null;

        return id;
    }
}
