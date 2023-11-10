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
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;

import static java.lang.Thread.sleep;

import android.view.KeyEvent;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.inventorymanager.ui.filter.chooseFilterFragment;
import com.example.inventorymanager.ui.filter.filteredItemsFragment;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Conducts comprehensive UI testing. Tests the home view and the primary actions that can be taken
 * within the home view. Tests basic filtering, sorting, multi-selection, multi-deleting, and multi-tagging*.
 * @author Tyler Hoekstra, Kareem Assaf
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see chooseFilterFragment
 * @see filteredItemsFragment
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewTesting {

    @Rule
    public ActivityScenarioRule<LoginActivity> scenario = new
            ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by make of the items.
     */
    @Test
    public void testItemFilteringMake() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(7);
        // Filter by make
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.make_keyword_editText)).perform(typeText("Logitech"));
        onView(withId(R.id.searchButton)).perform(click());
        // Check if the test is successful
        // Checks to make sure all logitech items are displayed
        onView(withText("Gaming Keyboard")).check(matches(isDisplayed()));
        onView(withText("Gaming Mouse")).check(matches(isDisplayed()));
        onView(withText("Gaming Headset")).check(matches(isDisplayed()));
        onView(withText("My Ergo Mouse")).check(matches(isDisplayed()));
        // Check to make sure other items are not displayed
        onView(withText("Toy Car")).check(doesNotExist());
        onView(withText("Laptop")).check(doesNotExist());
        onView(withText("Smartphone")).check(doesNotExist());
        // Select and delete the filtered items
        for(int i = 0; i < 4; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withText("Gaming Keyboard")).check(doesNotExist());
        onView(withText("Gaming Mouse")).check(doesNotExist());
        onView(withText("Gaming Headset")).check(doesNotExist());
        onView(withText("My Ergo Mouse")).check(doesNotExist());
        // Return to home
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        // Select all items and delete them.
        for(int i = 0; i < 3; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by keywords in their description.
     */
    @Test
    public void testItemFilteringKeyword() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(7);
        // Filter by make
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.description_keyword_editText)).perform(typeText("Mouse"));
        onView(withId(R.id.searchButton)).perform(click());
        // Check if the test is successful
        // Checks to make sure all logitech items are displayed
        onView(withText("Gaming Mouse")).check(matches(isDisplayed()));
        onView(withText("My Ergo Mouse")).check(matches(isDisplayed()));
        // Check to make sure other items are not displayed
        onView(withText("Gaming Keyboard")).check(doesNotExist());
        onView(withText("Gaming Headset")).check(doesNotExist());
        onView(withText("Toy Car")).check(doesNotExist());
        onView(withText("Laptop")).check(doesNotExist());
        onView(withText("Smartphone")).check(doesNotExist());
        // Select and delete the filtered items
        for(int i = 0; i < 2; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withText("Gaming Mouse")).check(doesNotExist());
        onView(withText("My Ergo Mouse")).check(doesNotExist());
        // Return to home
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        // Select all items and delete them.
        for(int i = 0; i < 5; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Tests the item filtering in the home view. First login, adds multiple items, then attempt to filter
     * them by date of purchase.
     */
    @Test
    public void testItemFilteringDate() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(3);
        // Filter by date pf purchase
        onView(withId(R.id.filter_button)).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateString = dateFormat.format(currentDate);

        onView(withId(R.id.start_date_editText)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.start_date_editText)).check(matches(withText(currentDateString)));

        onView(withId(R.id.end_date_editText)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_date_editText)).check(matches(withText(currentDateString)));

        onView(withId(R.id.searchButton)).perform(click());
        // Check if the test is successful
        // Checks to make sure all items purchased today are displayed
        onView(withText("Gaming Mouse")).check(matches(isDisplayed()));
        onView(withText("Gaming Keyboard")).check(matches(isDisplayed()));
        onView(withText("Gaming Headset")).check(matches(isDisplayed()));
        // Check to make sure other items are not displayed
        // Select and delete the filtered items
        for(int i = 0; i < 3; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
        // Return to home
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        // Select all items and delete them.
        for(int i = 0; i < 0; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by make of the items.
     */
    @Test
    public void testItemSortingMake() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(6);
        // Filter by make
        onView(withId(R.id.sort_button)).perform(click());
        onView(withId(R.id.sort_make_button)).perform(click());
        onView(withId(R.id.sort_done)).perform(click());

        // first item should be Toy Car
        onData(anything())
                .inAdapterView(withId(R.id.item_list)).atPosition(0)
                .onChildView(withId(R.id.itemNameTextView)).check(matches(withText(containsString("Toy"))));

        // Select all items and delete them.
        for(int i = 0; i < 6; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());


    }

    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by keywords in the description of the items.
     */
    @Test
    public void testItemSortingKeyword() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(3);
        // Filter by make
        onView(withId(R.id.sort_button)).perform(click());
        onView(withId(R.id.sort_description_button)).perform(click());
        onView(withId(R.id.sort_done)).perform(click());

        // first item should be Toy Car
        onData(anything())
                .inAdapterView(withId(R.id.item_list)).atPosition(0)
                .onChildView(withId(R.id.itemNameTextView)).check(matches(withText(containsString("Headset"))));

        // Select all items and delete them.
        for(int i = 0; i < 3; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());

    }

    /**
     * Tests the item sorting in the home view. First login, adds multiple items, then attempt to sort
     * them by value of the items.
     */
    @Test
    public void testItemValueKeyword() {
        // Do Login first
        // Add all the items to the list
        loginAndAddManyItems(3);
        // Filter by make
        onView(withId(R.id.sort_button)).perform(click());
        onView(withId(R.id.sort_value_button)).perform(click());
        onView(withId(R.id.sort_done)).perform(click());

        // first item should be Toy Car
        onData(anything())
                .inAdapterView(withId(R.id.item_list)).atPosition(0)
                .onChildView(withId(R.id.itemNameTextView)).check(matches(withText(containsString("Mouse"))));

        // Select all items and delete them.
        for(int i = 0; i < 3; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());

    }

    /**
     * Tests selecting and deleting multiple items at once. First login, adds multiple items, selects
     * multiple items, then delete and check if the items were successfully deleted.
     */
    @Test
    public void testMultiSelectionDelete() {
        // Do Login first
        // Add all the items
        loginAndAddManyItems(3);
        for(int i = 0; i < 3; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
        // make sure to refresh page in case items still there
        onView(withId(R.id.navigation_addItem)).perform(click());
        onView(withId(R.id.navigation_home)).perform(click());
        // Check to make no items are displayed
        onView(withText("Gaming Mouse")).check(doesNotExist());
        onView(withText("Gaming Headset")).check(doesNotExist());
        onView(withText("Gaming Keyboard")).check(doesNotExist());

    }

    /**
     * Generates 7 unique items and adds them to the database.
     */
    private void loginAndAddManyItems(int num){
        // Click on the user name field
        onView(withId(R.id.username)).perform(click());
        // Type the username
        onView(withId(R.id.username)).perform(typeText("ViewTest"));
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
                break;
            } catch (androidx.test.espresso.NoMatchingViewException e) {
                // if this fails, then wait for a second and try again, maximum 10 times
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }

        // Add all the items
        if(num>0) {
        addItem("Gaming Keyboard", null, "Keyboard for gaming", "Logitech",
                "Apex Pro", "123456FGHJ", "200.00", "Cool Keyboard");}
        if(num>1) {
        addItem("Gaming Mouse", null,"Mouse for gaming", "Logitech",
                "G502 Lightspeed", "ABC123FG45", "180.00", "Cool Mouse");}
        if(num>2) {
        addItem("Gaming Headset", null, "Headset for gaming", "Logitech",
                "Astro A30", "PJF9920", "230.99", "My Headset");}
        if(num>3) {
        addItem("Toy Car", null,"My little toy car", "Hot Wheels",
                "Rocket Car", "MNA67", "14.50", "My toy car");}
        if(num>4) {
        addItem("My Ergo Mouse", null, "Mouse for coding", "Logitech",
                "MX Master 3S", "JIN879T", "139.99", "Nice Mouse");}
        if(num>5) {
        addItem("Laptop", null, "Laptop for school", "Microsoft",
                "Surface Pro 7", "UGB675", "1500.50", "Slow laptop");}
        if(num>6) {
        addItem("Smartphone", null, "My iPhone", "Apple",
                "iPhone 13 Pro", "79HJHU", "1100.00", "Newest phone");}

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
    private void addItem(String Name, @Nullable String currentDateString, String Description, String Make, String Model, String SNumber, String EValue, String
        Comment) {
        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText(Name));

        // Check the purchase date by clicking the current date on the date picker and checking if it
        // matches with the current date
        if (currentDateString == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = Calendar.getInstance().getTime();
            currentDateString = dateFormat.format(currentDate);
            onView(withId(R.id.purchaseDateInput)).perform(click());
            onView(withText("OK")).perform(click());
            onView(withId(R.id.purchaseDateInput)).check(matches(withText(currentDateString)));
        }
        else {
//            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
//                    .perform(PickerActions.setDate(2023, 11, 8));
        }


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
