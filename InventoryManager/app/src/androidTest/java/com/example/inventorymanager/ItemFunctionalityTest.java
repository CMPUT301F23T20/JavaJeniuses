package com.example.inventorymanager;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.KeyEvent;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * Conducts comprehensive UI testing on fragments and item-related functionalities
 * Verifies proper navigation between fragments
 * Assesses the functionality of adding, editing, and deleting items
 * @author Kareem Assaf
 * @see
 */
public class ItemFunctionalityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);


    /**
     * Test the navigation to the "Add Item" fragment and adding an item.
     */
    @Test
    public void testAddItemNav(){
        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText("Gaming Keyboard"));

        // Check the purchase date by clicking the current date on the date picker and checking if it
        // matches with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateString = dateFormat.format(currentDate);
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.purchaseDateInput)).check(matches(withText(currentDateString)));

        // Set the item's description
        onView(withId(R.id.descriptionInput)).perform(typeText("Keyboard for gaming"));

        // Set the item's make
        onView(withId(R.id.makeInput)).perform(typeText("Logitech"));

        // Set the item's model
        onView(withId(R.id.modelInput)).perform(typeText("Apex Pro"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.serialNumberInput)).perform(typeText("1A2B3C4D5E"));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's estimate value
        onView(withId(R.id.estimatedValueInput)).perform(typeText("200.00"));
        onView(withId(R.id.estimatedValueInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText("Nice keyboard"));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());

        // Check to ensure navigation back to home fragment
        onView(withText("Add Tag")).check(matches(isDisplayed()));


    }

    /**
     * Test the navigation to delete an item.
     * In order to delete an item, you must navigate to view item so this function also tests that
     */
    @Test
    public void testDeleteItemNav() { // For some reason this only works with this method name
                                    // If you change the method name you get an error

        // Show the listview from Firestore
        onView(withId(R.id.navigation_addItem)).perform(click());
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withText("Gaming Keyboard")).perform(scrollTo());

        // Click on the listview item with the text of "Gaming Keyboard"
        onView(withText("Gaming Keyboard")).perform(click());

        // Scroll to the delete button and delete the item
        onView(withId(R.id.deleteButton)).perform(scrollTo());
        onView(withId(R.id.deleteButton)).perform(click());
    }

}