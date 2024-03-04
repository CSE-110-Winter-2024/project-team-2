package edu.ucsd.cse110.successorator.app.ui.context;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.lib.domain.GoalContext;

/**
 * This class maintains the list of goal contexts available to select.
 */
public class GoalContextListAdapter extends RecyclerView.Adapter<GoalContextListAdapter.ViewHolder> {
    /**
     * This inner class manages the container holding a context circle item in the list. The GoalContextListAdapter
     * is parameterized by the generic type for its elements; we use our ViewHolder class for that.
     * Used Android documentation for RecyclerView.Adapter with example code from:
     * <a href="https://github.com/android/views-widgets-samples/blob/main/RecyclerView/Application/src/main/java/com/example/android/recyclerview/CustomAdapter.java">...</a>
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button goalContextButton;

        public ViewHolder(View v) {
            super(v);

            goalContextButton = (Button) v.findViewById(R.id.goalContextButton);
        }

        public Button getGoalContextButton() {
            return goalContextButton;
        }
    }

    List<GoalContext> goalContexts;
    Consumer<Integer> onClick;
    private MainViewModel activityModel;

    // Brightness (alpha channel) of background color when a goal context button is or is not selected. Max is 0xFF (255)
    private static final int SELECTED_BRIGHTNESS = 0xFF;
    private static final int UNSELECTED_BRIGHTNESS = 0x55;

    public GoalContextListAdapter(Fragment fragment, List<GoalContext> contexts, Consumer<Integer> onClick) {
        /*
         * This sets a bunch of stuff internally, which we can access
         * with getContext() and getItem() for example.
         *
         * Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
         * or it will crash!
         */
        this.goalContexts = new ArrayList<>(contexts);
        this.onClick = onClick;

        // Initialize Model
        var modelOwner = fragment.requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.goal_context_circle, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // The goal contexts have IDs 1, 2, 3, 4, so the goal context ID is index + 1
        int goalContextId = position + 1;
        var goalContext = GoalContext.getGoalContextById(goalContextId);

        // Use ViewHolder instance to retrieve reference to goal context button
        Button goalContextButton = holder.getGoalContextButton();

        // Use first letter of goal context name as text to display on button
        goalContextButton.setText(Character.toString(goalContext.getFirstLetterOfName()));

        // Set button background color to goal context's color, but slightly dimmer when not selected
        int goalContextColor = Color.parseColor(goalContext.getColor());
        int color = Color.argb(UNSELECTED_BRIGHTNESS, Color.red(goalContextColor), Color.green(goalContextColor), Color.blue(goalContextColor));
        // Note that we cannot simply use goalContextButton.setBackgroundColor(), as that would override the circular button shape.
        goalContextButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        activityModel.getSelectedGoalContextId().observe(selectedGoalContextId -> {
            // When the selected context ID is updated, check whether this context circle is selected
            boolean isSelected = (selectedGoalContextId != null && selectedGoalContextId == goalContextId);

            // Update context circle background color to be brighter if selected
            int m_goalContextColor = Color.parseColor(goalContext.getColor());
            int m_color = Color.argb(isSelected ? SELECTED_BRIGHTNESS : UNSELECTED_BRIGHTNESS, Color.red(m_goalContextColor), Color.green(m_goalContextColor), Color.blue(m_goalContextColor));
            goalContextButton.getBackground().setColorFilter(m_color, PorterDuff.Mode.MULTIPLY );
        });

        // Listen for clicks on goal context button, forward to our onClick() attribute
        goalContextButton.setOnClickListener(v -> {
            onClick.accept(goalContextId);
        });
    }

    @Override
    public int getItemCount() {
        return goalContexts.size();
    }
}
