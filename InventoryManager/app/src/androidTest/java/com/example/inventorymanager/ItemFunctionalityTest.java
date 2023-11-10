package com.example.inventorymanager;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
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

import android.app.Activity;
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
    public ActivityScenarioRule<LoginActivity> scenario = new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Test that the uer can login to the application.
     * Tests US 6.1.
     */
    @Test
    public void testLoginNav(){
        // Click on the user name field
        onView(withId(R.id.username)).perform(click());
        // Type the username
        onView(withId(R.id.username)).perform(typeText("JohnDoe"));
        onView(withId(R.id.username)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Click on the password field
        // Type the password
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.password)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Login to the user account
        onView(withId(R.id.login)).perform(click());

        // wait to ensure that Firebase has time to process the login
        for (int i = 0; i < 10; i++) {
            // try and see if login has loaded yet
            try {
                // try to move to the add item fragment bu pressing the button
                onView(withId(R.id.navigation_addItem)).perform(click());
            } catch (androidx.test.espresso.NoMatchingViewException e) {
                // if this fails, then wait for a second and try again, maximum 10 times
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }

        // verify that we are on the next page
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));
        onView(withText("Add Tag")).check(matches(isDisplayed()));
    }


    /**
     * Test the navigation to the "Add Item" fragment and adding an item.
     * Tests US 1.1, 2.1, and 2.2.
     */
    @Test
    public void testAddItemNav(){
        testLoginNav();

        // check that estimated value field starts as empty
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));

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
        onView(withId(R.id.descriptionInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's make
        onView(withId(R.id.makeInput)).perform(typeText("Logitech"));
        onView(withId(R.id.makeInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

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
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).check(matches(isDisplayed()));

        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
    }


    /**
     * Test the navigation to edit an item and editing an item
     * In order to edit an item, you must navigate to view item so this function also tests that.
     * Tests US 1.2, 1.3, 2.1, and 2.2.
     */
    @Test
    public void testEditItemNav() {
        testLoginNav();

        // check that estimated value field starts with last item
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));

        // Scroll and click the item to edit
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).perform(click());

        onView(withId(R.id.editButton)).perform(scrollTo());
        onView(withId(R.id.editButton)).perform(click());

        // Edit the item's name
        onView(withId(R.id.itemNameInput)).perform(clearText());
        onView(withId(R.id.itemNameInput)).perform(typeText("Car"));

        // Edit the item's description
        onView(withId(R.id.descriptionInput)).perform(clearText());
        onView(withId(R.id.descriptionInput)).perform(typeText("Car for driving"));
        onView(withId(R.id.descriptionInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Edit the item's make
        onView(withId(R.id.makeInput)).perform(clearText());
        onView(withId(R.id.makeInput)).perform(typeText("Honda"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Edit the item's model
        onView(withId(R.id.modelInput)).perform(clearText());
        onView(withId(R.id.modelInput)).perform(typeText("Civic"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Edit the item's serial number
        onView(withId(R.id.serialNumberInput)).perform(clearText());
        onView(withId(R.id.serialNumberInput)).perform(typeText("12345ABCDE"));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Edit the item's estimate value
        onView(withId(R.id.estimatedValueInput)).perform(clearText());
        onView(withId(R.id.estimatedValueInput)).perform(typeText("5000.00"));
        onView(withId(R.id.estimatedValueInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(clearText());
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText("Reliable Car"));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));


        // Save the edited item
        onView(withId(R.id.saveButton)).perform(scrollTo());
        onView(withId(R.id.saveButton)).perform(click());
        // Check to ensure navigation back to home fragment
        onView(withText("Add Tag")).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("Car")).perform(scrollTo());
        onView(withText("Car")).check(matches(isDisplayed()));

        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$5,000.00")));
    }



    /**
     * Test the navigation to delete an item and deleting an item.
     * In order to delete an item, you must navigate to view item so this function also tests that.
     * Tests US 1.2, 1.4, 2.1, and 2.2.
     */
    @Test
    public void testDeleteItemNav() { // For some reason this only works with this method name
        // If you change the method name you get an error
        testLoginNav();

        // check that estimated value field displays previous value
        onView(withId(R.id.total_value)).check(matches(withText("$5,000.00")));

        // Show the listview from Firestore
        onView(withText("Car")).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText("Car")).perform(click());

        // Scroll to the delete button and delete the item
        onView(withId(R.id.deleteButton)).perform(scrollTo());
        onView(withId(R.id.deleteButton)).perform(click());

        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }

}
