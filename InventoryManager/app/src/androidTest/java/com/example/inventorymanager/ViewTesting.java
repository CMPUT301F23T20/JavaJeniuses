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
import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * Conducts comprehensive UI testing. Tests the home view and the primary actions that can be taken
 * within the home view. Tests basic filtering, sorting, multi-selection, multi-deleting, and multi-tagging*.
 * @author Tyler Hoekstra, Kareem Assaf
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see chooseFilterFragment
 * @see filteredItemsFragment
 */
public class ViewTesting {

    @Rule
    public ActivityScenarioRule<LoginActivity> scenario = new
            ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by make of the items.
     */
    public void testItemFilteringMake() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems();
        // Filter by make
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.make_keyword_editText)).perform(typeText("Logitech"));
        onView(withId(R.id.searchButton)).perform(click());
        // Check if the test is successful

        // Return to home

        // Select all items and delete them.


    }

    @Test
    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by keywords in their description.
     */
    public void testItemFilteringKeyword() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.description_keyword_editText)).perform(typeText("Mouse"));
        onView(withId(R.id.searchButton)).perform(click());
        // Check if the test is successful

        // Return to home

        // Select all items and delete them.
    }

    @Test
    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by date of purchase.
     */
    public void testItemFilteringDate() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();
        // Check if the test is successful

        // Return to home

        // Select all items and delete them.

    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by make of the items.
     */
    public void testItemSortingMake() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();

    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by keywords in the description of the items.
     */
    public void testItemSortingKeyword() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();

    }

    @Test
    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by date of purchase of the items.
     */
    public void testItemSortingDate() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();

    }

    @Test
    /**
     * Tests selecting and deselecting of multiple items. First login, adds multiple items, selects
     * multiple items, check for selection, deselects multiple items, checks for selection.
     */
    public void testMultiSelection() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();


    }

    @Test
    /**
     * Tests selecting and deleting multiple items at once. First login, adds multiple items, selects
     * multiple items, then delete and check if the items were successfully deleted.
     */
    public void testMultiDelete() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems();
        



    }

    @Test
    /**
     *
     */
    public void testTagging() {
        // Unneeded for current implementation
        // Do Login first
    }

    /**
     * Generates 7 unique items and adds them to the database.
     */
    private void loginAndAddManyItems(){
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
        // Add all the items
        addItem("Gaming Keyboard", "Keyboard for gaming", "Logitech",
                "Apex Pro", "123456FGHJ", "200.00", "Cool Keyboard");
        addItem("Gaming Mouse", "Mouse for gaming", "Logitech",
                "G502 Lightspeed", "ABC123FG45", "180.00", "Cool Mouse");
        addItem("Gaming Headset", "Headset for gaming", "Logitech",
                "Astro A30 Wireless", "PJF9920", "230.99", "My Headset");
        addItem("Toy Car", "My little toy car", "Hot Wheels",
                "Rocket League Car", "MNA67", "14.50", "My toy car");
        addItem("My Ergo Mouse ", "Mouse for coding", "Logitech",
                "MX Master 3S", "JIN879T", "139.99", "Nice Mouse");
        addItem("Laptop", "Laptop for school", "Microsoft",
                "Surface Pro 7", "UGB675", "1500.50", "Slow laptop");
        addItem("Smartphone", "My iPhone", "Apple",
                "iPhone 13 Pro", "79HJHU", "1100.00", "Newest phone");
    }

    /**
     * Adds items through a function to reduce the amount of code duplication in testing.
     * @param Name The name of the item to be added.
     * @param Description The description of the item to be added.
     * @param Make The make of the item to be added.
     * @param Model The model of the item to be added.
     * @param SNumber The serial number of the item to be added.
     * @param EValue The estimated value of the item to be added.
     * @param Comment A comment for the item to be added.
     */
    private void addItem(String Name, String Description, String Make, String Model, String SNumber, String EValue, String
        Comment) {
        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText(Name));

        // Check the purchase date by clicking the current date on the date picker and checking if it
        // matches with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateString = dateFormat.format(currentDate);
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.purchaseDateInput)).check(matches(withText(currentDateString)));

        // Set the item's description
        onView(withId(R.id.descriptionInput)).perform(typeText(Description));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's make
        onView(withId(R.id.makeInput)).perform(typeText(Make));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's model
        onView(withId(R.id.modelInput)).perform(typeText(Model));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.serialNumberInput)).perform(typeText(SNumber));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's estimate value
        onView(withId(R.id.estimatedValueInput)).perform(typeText(EValue));
        onView(withId(R.id.estimatedValueInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText(Comment));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());
    }

}
