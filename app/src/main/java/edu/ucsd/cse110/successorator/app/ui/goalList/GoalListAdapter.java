package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.GoalListItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * This class maintains the list of goals
 */
public class GoalListAdapter extends ArrayAdapter<Goal> {
    Consumer<Goal> onClick;

    public GoalListAdapter(Context context, List<Goal> goals, Consumer<Goal> onClick) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(goals));
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the goal for this position.
        var goal = getItem(position);
        assert goal != null;

        // Check if a view is being reused...
        GoalListItemBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = GoalListItemBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = GoalListItemBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the goal's data.
        binding.goalTextView.setText(goal.getGoalText());

        // Bind the goal text view to the callback.
        binding.goalTextView.setOnClickListener(v -> {
//            var id = goal.getId();
//            assert id != null;
//            onClick.accept(id);
            onClick.accept(goal);
        });


        return binding.getRoot();
    }

    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
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
