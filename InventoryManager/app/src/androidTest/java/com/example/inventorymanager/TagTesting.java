package com.example.inventorymanager;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.annotation.Nullable;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Conducts comprehensive UI testing on item tags.
 * Verifies that tags can be ____
 * Tests US 3.1, 3.2, 3.3, 3.4, 3.5
 * All tests should be run on Pixel 6 API 34.
 * @author Sumaiya Salsabil
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TagTesting {
    @Rule
    public ActivityScenarioRule<LoginActivity> scenario = new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Tests that users can create tags to categorize items
     * Tests US 3.1.
     */
    @Test
    public void testCreateTag() {
        login();
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        createTag("Tag 1", "purple");

        onView(withId(R.id.autocomplete_textview)).perform(click());
        onView(withText("Tag 1"))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()));
//                .perform(click());
    }

    /**
     * Tests that create tag error message appears correctly.
     * Tests US 3.1.
     */
    @Test
    public void testCreateTagError() {
        login();
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        createTag("", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("This field is required")));

        createTag("Way too long", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("Up to 10 characters")));

        createTag("C,omma", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("Commas are illegal")));

        createTag("Semi;colon", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("Semicolons are illegal")));

    }

    /**
     * Tests that creation of a tag can be cancelled
     * Tests US 3.1.
     */
    @Test
    public void testCreateTagCancel() {
        login();
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        onView(withId(R.id.cancelButton)).perform(click());
        onView(withId(R.id.create_tag_button)).check(matches(isDisplayed()));
        onView(withText("Create Tag")).check(matches(isDisplayed()));

    }

    /**
     * Tests that users can add a tag to an item while adding the item.
     * Tests US 3.2.
     */
    @Test
    public void testAddItemWithTag() {
        login();
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        String tagText = "Device";
        createTag(tagText, "blue");
        onView(withId(R.id.add_tag_button)).perform(click());
        //onView(withId(R.id.navigation_addItem)).perform(click());
        //addItemWithTag(tagText);
        onView(withText("Gaming Keyboard")).perform(click());
        onView(withText(tagText)).check(matches(isDisplayed()));
//        onView(withId(R.id.tagList)).check(matches(hasDescendant(withText(tagText))));
        deleteItem();
    }

    /**
     * Tests that users can sort the list of items by tags.
     * Tests US 3.4.
     */
    @Test
    public void testSortByTag() {
        login();
        addManyItems(2);

        // sort by tag
        onView(withId(R.id.sort_button)).perform(click());
        onView(withId(R.id.sort_tag_button)).perform(click());
        onView(withId(R.id.sort_done)).perform(click());

        // first item should be Gaming Mouse
        onData(anything())
                .inAdapterView(withId(R.id.item_list)).atPosition(0)
                .onChildView(withId(R.id.itemNameTextView)).check(matches(withText(containsString("Mouse"))));

        // Select all items and delete them.
        for(int i = 0; i <2; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Logs in the default user, JohnDoe.
     */
    private void login() {
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
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
                // try to move to the add item fragment by pressing the button
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

        // verify that we are on the Add Tag page
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));
        onView(withText("Add Tag")).check(matches(isDisplayed()));
    }

    private void createTag(String tagText, String tagColour) {

        onView(withId(R.id.tagEditText)).perform(clearText());
        onView(withId(R.id.tagEditText)).perform(typeText(tagText));
        closeSoftKeyboard();

        if (tagColour == "red") { onView(withId(R.id.red_button)).perform(click()); }
        if (tagColour == "orange") { onView(withId(R.id.orange_button)).perform(click()); }
        if (tagColour == "yellow") { onView(withId(R.id.yellow_button)).perform(click()); }
        if (tagColour == "blue") { onView(withId(R.id.blue_button)).perform(click()); }
        if (tagColour == "purple") { onView(withId(R.id.purple_button)).perform(click()); }
        if (tagColour == "pink") { onView(withId(R.id.pink_button)).perform(click()); }
        onView(withId(R.id.saveButton)).perform(click());
    }

    private void addItemWithTag(String tagText) {
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

        // Add a tag
        onView(withId(R.id.autocomplete_textview_in_add_item)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());
        // Give time for fragment to load
        SystemClock.sleep(2000);
        // Check to ensure navigation back to home fragment
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).check(matches(isDisplayed()));
    }

    private void deleteItem(){
        // Show the listview from Firestore
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText("Gaming Keyboard")).perform(click());

        // Scroll to the delete button and delete the item
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.deleteButton)).perform(click());
    }

    private void scrollToTop() {
        try {
            // Create a UiScrollable instance
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // Scroll down by swiping up
            scrollable.scrollBackward();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows to scroll down to the bottom of the fragment
     */
    private void scrollToBottom() {
        try {
            // Create a UiScrollable instance
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // Scroll down by swiping up
            scrollable.scrollForward();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
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
            Comment, String tagText) {
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

        // Add a tag
        onView(withId(R.id.autocomplete_textview_in_add_item)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText(Comment));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());
    }

    private void addManyItems(int num){
        if(num>0) {
            addItem("Gaming Keyboard", null, "Keyboard for gaming", "Logitech",
                    "Apex Pro", "123456FGHJ", "200.00", "Cool Keyboard", "Device");}
        if(num>1) {
            addItem("Gaming Mouse", null,"Mouse for gaming", "Logitech",
                    "G502 Lightspeed", "ABC123FG45", "180.00", "Cool Mouse", null);}
//        if(num>2) {
//            addItem("Gaming Headset", null, "Headset for gaming", "Logitech",
//                    "Astro A30", "PJF9920", "230.99", "My Headset");}
//        if(num>3) {
//            addItem("Toy Car", null,"My little toy car", "Hot Wheels",
//                    "Rocket Car", "MNA67", "14.50", "My toy car");}
//        if(num>4) {
//            addItem("My Ergo Mouse", null, "Mouse for coding", "Logitech",
//                    "MX Master 3S", "JIN879T", "139.99", "Nice Mouse");}
//        if(num>5) {
//            addItem("Laptop", null, "Laptop for school", "Microsoft",
//                    "Surface Pro 7", "UGB675", "1500.50", "Slow laptop");}
//        if(num>6) {
//            addItem("Smartphone", null, "My iPhone", "Apple",
//                    "iPhone 13 Pro", "79HJHU", "1100.00", "Newest phone");}
    }

}
