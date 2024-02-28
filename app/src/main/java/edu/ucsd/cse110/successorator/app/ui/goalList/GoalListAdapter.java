package edu.ucsd.cse110.successorator.app.ui.goalList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.app.databinding.GoalListItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * This class maintains the list of goals
 */
public class GoalListAdapter extends ArrayAdapter<Goal> {
    Consumer<Integer> onClick;

    public GoalListAdapter(Context context, List<Goal> goals, Consumer<Integer> onClick) {
       /*
        * This sets a bunch of stuff internally, which we can access
        * with getContext() and getItem() for example.
        *
        * Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        * or it will crash!
        */
        super(context, 0, new ArrayList<>(goals));
        this.onClick = onClick;
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

        // Display as strikethrough if isComplete is true
        if (goal.getIsComplete()) {
            binding.goalTextView.setPaintFlags(binding.goalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.goalTextView.setPaintFlags(binding.goalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Bind the goal text view to the callback.
        binding.goalTextView.setOnClickListener(v -> {
            var id = goal.getId();
            // Check if it's a pending goal here
            onClick.accept(id);

            TextView textView = (TextView) v;
            int flags = textView.getPaintFlags();
            // Toggle the strike through
            if ((flags & Paint.STRIKE_THRU_TEXT_FLAG) == Paint.STRIKE_THRU_TEXT_FLAG) {
                textView.setPaintFlags(flags & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                textView.setPaintFlags(flags | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

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

    @Override
    public long getItemId(int position) {
        var goal = getItem(position);
        assert goal != null;

        var id = goal.getId();
        assert id != null;

        return id;
    }
}
