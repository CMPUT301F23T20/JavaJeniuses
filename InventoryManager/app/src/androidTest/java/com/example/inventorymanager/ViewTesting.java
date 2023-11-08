package com.example.inventorymanager;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * Conducts comprehensive UI testing. Tests the home view and the primary actions that can be taken
 * within the home view. Tests basic displaying, filtering, sorting, multi-selection, multi-deleting,
 * and multi-tagging*.
 * @author Tyler Hoekstra
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see chooseFilterFragment
 * @see filteredItemsFragment
 */
public class ViewTesting {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);


    @Test
    /**
     *
     */
    public void testItemDisplaying(){
        // Do Login first
        
    }


    @Test
    /**
     *
     */
    public void testItemFiltering(){
        // Do Login first
    }


    @Test
    /**
     *
     */
    public void testItemSorting(){
        // Do Login first
    }

    @Test
    /**
     *
     */
    public void testMultiSelection(){
        // Do Login first
    }

    @Test
    /**
     *
     */
    public void testMultiDelete(){
        // Do Login first
    }

    @Test
    /**
     *
     */
    public void testTagging() {
        // Unneeded for current implementation
        // Do Login first
    }

}
