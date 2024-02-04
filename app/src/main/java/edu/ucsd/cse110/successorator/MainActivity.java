package edu.ucsd.cse110.successorator;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the default text for initial screen with no goals
        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.placeholderText.setText(R.string.default_text);

        //displays default screen for no goals
        setContentView(view.getRoot());
    }

    //Method to display the add menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_bar, menu);
        return true;
    }

    //Method for switching view to add a goal
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        var itemId = item.getItemId();

        //When the add button is clicked, switch view to the enter_goal view
        if (itemId == R.id.add_bar_menu_swap_views){
            setContentView(R.layout.enter_goal);
        }
        return super.onOptionsItemSelected(item);
    }

}
