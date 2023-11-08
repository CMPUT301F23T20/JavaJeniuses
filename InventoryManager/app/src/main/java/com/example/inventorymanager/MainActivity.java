package com.example.inventorymanager;

import android.content.Intent;
import android.app.ActionBar;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inventorymanager.databinding.ActivityMainBinding;

/**
 * The main activity in which the entire application operates.
 * Provides the framework of the application, including the back button and navigation tabs.
 * Transfers control to the application fragments that provide the application's functionality for the rest of its life cycle.
 * @author Kareem Assaf, Tomasz Ayobahan, Tyler Hoekstra, Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    /**
     * Initially sets up the application's operations.
     * @param savedInstanceState The previous state of the application; not used in this application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);

//        String myUser = getIntent().getStringExtra("username");
//        ItemViewModel temp = new ItemViewModel();
//        temp.getUserItems(myUser);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_addItem, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Enables the back button present in the top left corner of nested fragments to have an effect.
     * In all cases, this back button causes the user to return to the app home screen (item list).
     * @return TRUE in all cases.
     */
        // based on code from Stack Overflow, License: Attribution-ShareAlike 4.0 International
        // published April 2019, accessed November 2023
        // https://stackoverflow.com/questions/24032956/action-bar-back-button-not-working
    @Override
    public boolean onSupportNavigateUp() {
        // always just navigate back to the home screen when pressed
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);
        return true;
    }
}