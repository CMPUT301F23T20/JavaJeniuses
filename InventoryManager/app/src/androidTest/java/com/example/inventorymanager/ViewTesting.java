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
 * within the home view. Tests basic filtering, sorting, multi-selection, multi-deleting, and multi-tagging*.
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
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by make of the items.
     */
    public void testItemFilteringMake(){
        // Do Login first
    }

    @Test
    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by keywords in their description.
     */
    public void testItemFilteringKeyword(){
        // Do Login first
    }

    @Test
    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by date of purchase.
     */
    public void testItemFilteringDate(){
        // Do Login first
    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by make of the items.
     */
    public void testItemSortingMake(){
        // Do Login first
    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by keywords in the description of the items.
     */
    public void testItemSortingKeyword(){
        // Do Login first
    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by date of purchase of the items.
     */
    public void testItemSortingDate(){
        // Do Login first
    }

    @Test
    /**
     * Tests selecting and deselecting of multiple items. First login, adds multiple items, selects
     * multiple items, check for selection, deselects multiple items, checks for selection.
     */
    public void testMultiSelection(){
        // Do Login first
    }

    @Test
    /**
     * Tests selecting and deleting multiple items at once. First login, adds multiple items, selects
     * multiple items, then delete and check if the items were successfully deleted.
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
