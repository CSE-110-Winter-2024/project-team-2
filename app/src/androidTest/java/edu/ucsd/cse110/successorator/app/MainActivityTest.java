package edu.ucsd.cse110.successorator.app;

import static junit.framework.TestCase.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.app.databinding.FragmentNoGoalsBinding;

/**
 * Tests for the rendering of the MainActivity class.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Test
    public void displaysNoGoalsText() {
        try (var scenario = ActivityScenario.launch(MainActivity.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onActivity(activity -> {
                var rootView = activity.findViewById(R.id.root);
                var binding = FragmentNoGoalsBinding.bind(rootView);

                var expected = activity.getString(R.string.noGoalsText);
                var actual = binding.placeholderText.getText();

                assertEquals(expected, actual);
            });

            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }
}