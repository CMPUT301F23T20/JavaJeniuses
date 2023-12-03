package com.example.inventorymanager;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import android.os.SystemClock;
import android.view.KeyEvent;
import androidx.annotation.Nullable;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.example.inventorymanager.ui.filter.chooseFilterFragment;
import com.example.inventorymanager.ui.filter.filteredItemsFragment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.notNullValue;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.KeyEvent;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        //  Navigate to the tag screen and open the tag creation dialog
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        // Create a tag with the name "Tag 1" and color "purple"
        createTag("Tag 1", "purple");

        // Open the autocomplete dropdown and check if the created tag "Tag 1" is displayed
        onView(withId(R.id.autocomplete_textview)).perform(click());
        onView(withText("Tag 1"))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    /**
     * Tests that create tag error messages appear correctly.
     * Tests US 3.1.
     */
    @Test
    public void testCreateTagError() {
        login();

        // Navigate to the tag screen and open the tag creation dialog
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        // Verify error message when tag text is empty
        createTag("", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("This field is required")));

        // Verify error message when tag text is too long
        createTag("Way too long", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("Up to 10 characters")));

        // Verify error message when tag text includes commas
        createTag("C,omma", "pink");
        onView(withId(R.id.tagEditText)).check(matches(hasErrorText("Commas are illegal")));

        // Verify error message when tag text includes semicolons
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

        // Navigate to the tag screen and open the tag creation dialog
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());

        // Cancel the tag creation process and verify that it went back to the Add Tag page
        onView(withId(R.id.cancelButton)).perform(click());
        onView(withId(R.id.create_tag_button)).check(matches(isDisplayed()));
        onView(withText("Create Tag")).check(matches(isDisplayed()));
    }

    /**
     * Tests that users can select items from list and add tags
     * Tests US 3.2 and 3.3.
     */
    @Test
    public void testAddTags() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        login();

        // Add two items without tags
        addManyItemsWithoutTag(2);

        // Select those two items using checkboxes
        for(int i = 0; i <2; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }

        // Navigate to the tag screen & create a tag
        String tagText = "Device";
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        createTag(tagText, "blue");

        // Add the tag to the selected items
        SystemClock.sleep(500);
        onView(withId(R.id.autocomplete_textview)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.add_tag_button)).perform(click());

        // Check if the tag is displayed for the first two items
        SystemClock.sleep(1000);
        for (int i = 0; i < 2; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.item_list))
                    .atPosition(i)
                    .onChildView(withText(tagText))
                    .check(matches(isDisplayed()));
        }

        // Delete all the items
        SystemClock.sleep(500);
        for(int i = 0; i <2; i++) {
            onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(i).onChildView(withId(R.id.checkBox)).perform(click());
        }
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Tests that users can view an item's tags from View Item.
     * Tests US 3.2 and 3.3.
     */
    @Test
    public void testViewTagFromViewItem() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        login();

        // Navigate to the tag screen and create a new tag
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        String tagText = "Device";
        createTag(tagText, "blue");
        onView(withId(R.id.add_tag_button)).perform(click());

        // add item with tag
        addItemWithTag(tagText);

        // click on item to view details
        onView(withText("Gaming Keyboard")).perform(click());
        SystemClock.sleep(500);
        scrollToBottom();
        SystemClock.sleep(500);

        // check if tag is on view items page
        onView(allOf(withId(R.id.tagListInViewItem), withText(tagText)))
                .check(matches(isDisplayed()));

        // delete item
        SystemClock.sleep(500);
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Tests that users can add a tag to an item while adding the item.
     * Tests US 3.2.
     */
    @Test
    public void testAddItemWithTag() {
        login();

        // Navigate to the tag screen and create a new tag
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        String tagText = "Device";
        createTag(tagText, "blue");
        onView(withId(R.id.add_tag_button)).perform(click());

        // add item with tag
        addItemWithTag(tagText);

        // check if tag is on view items page
        onView(withText("Gaming Keyboard")).perform(click());
        onView(allOf(withId(R.id.tagListInViewItem), withText(tagText)))
                .check(matches(isDisplayed()));

        // delete item
        deleteItem("Gaming Keyboard");
    }

    /**
     * Tests that users can add a tag to an item from the Edit Item fragment.
     * Tests US 3.2 and 3.3.
     */
    @Test
    public void testAddTagFromEditItem() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        login();

        // Navigate to the tag screen and create a new tag
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        String tagText = "Device";
        createTag(tagText, "blue");
        onView(withId(R.id.add_tag_button)).perform(click());

        addItemWithoutTag();

        // press item to view details
        onView(withText("Gaming Keyboard")).perform(click());
        SystemClock.sleep(500);
        scrollToBottom();
        SystemClock.sleep(500);

        // navigate to edit item page
        onView(withId(R.id.editButton)).perform(click());
        scrollToBottom();
        SystemClock.sleep(500);

        // add tag from edit item page
        onView(withId(R.id.autocomplete_textview_in_edit_item_add_tag)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());

        // Save the edited item
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.saveButton)).perform(click());

        // Give time for fragment to load
        SystemClock.sleep(2000);
        // Check to ensure navigation back to home fragment
        onView(withText("Add Tag")).check(matches(isDisplayed()));

        // check if tag is added to item
        onView(allOf(withId(R.id.tagList), withText(tagText)))
                .check(matches(isDisplayed()));

        deleteItem("Gaming Keyboard");
    }

    /**
     * Tests that users can delete an item's tag from the Edit Item fragment.
     */
    @Test
    public void testDeleteTagFromEditItem() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        login();

        // Navigate to the tag screen and create a new tag
        onView(withId(R.id.tag_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        String tagText = "Device";
        createTag(tagText, "blue");
        onView(withId(R.id.add_tag_button)).perform(click());

        addItemWithTag(tagText);

        // navigate to edit item page
        onView(withText("Gaming Keyboard")).perform(click());
        SystemClock.sleep(500);
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.editButton)).perform(click());

        // choose tag to delete
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.autocomplete_textview_in_edit_item_delete_tag)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());

        // Save the edited item
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.saveButton)).perform(click());

        // Give time for fragment to load
        SystemClock.sleep(2000);
        // Check to ensure navigation back to home fragment
        onView(withText("Add Tag")).check(matches(isDisplayed()));

        // check if tag was deleted
        onView(withText(tagText)).check(doesNotExist());

        deleteItem("Gaming Keyboard");
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
     * Tests that users can filter the list of items by tags.
     * Tests US 3.5.
     */
    @Test
    public void testFilterByTag() {
        login();
        addManyItems(2);

        // filter by tag "Device"
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.autocomplete_textview_in_choose_filter)).perform(click());
        onView(withText("Device"))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.searchButton)).perform(click());

        // only item should be Gaming Headset
        onData(anything())
                .inAdapterView(withId(R.id.item_list)).atPosition(0)
                .onChildView(withId(R.id.itemNameTextView)).check(matches(withText(containsString("Headset"))));

        // Check that there are no other items on the screen
        onView(withText("Gaming Mouse")).check(doesNotExist());

        // Return to home page
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

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

    /**
     * Creates a tag with the specified text and color.
     *
     * @param tagText   The text content of the tag.
     * @param tagColour The color of the tag.
     */
    private void createTag(String tagText, String tagColour) {

        // enter tag text
        onView(withId(R.id.tagEditText)).perform(clearText());
        onView(withId(R.id.tagEditText)).perform(typeText(tagText));
        closeSoftKeyboard();

        // choose tag colour
        if (tagColour == "red") { onView(withId(R.id.red_button)).perform(click()); }
        if (tagColour == "orange") { onView(withId(R.id.orange_button)).perform(click()); }
        if (tagColour == "yellow") { onView(withId(R.id.yellow_button)).perform(click()); }
        if (tagColour == "blue") { onView(withId(R.id.blue_button)).perform(click()); }
        if (tagColour == "purple") { onView(withId(R.id.purple_button)).perform(click()); }
        if (tagColour == "pink") { onView(withId(R.id.pink_button)).perform(click()); }

        // save tag
        onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Adds a new item with the specified tag to the inventory.
     *
     * @param tagText The text content of the tag to be added to the item.
     */
    private void addItemWithTag(String tagText) {
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

    /**
     * Adds a new item without a specified tag to the inventory.
     *
     */
    private void addItemWithoutTag() {
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
        // Give time for fragment to load
        SystemClock.sleep(2000);
        // Check to ensure navigation back to home fragment
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).check(matches(isDisplayed()));
    }


    /**
     * Deletes the specified item from the inventory.
     *
     * @param item The name of the item to be deleted.
     */
    private void deleteItem(String item){
        // Show the listview from Firestore
        onView(withText(item)).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText(item)).perform(click());

        // Scroll to the delete button and delete the item
        scrollToBottom();
        SystemClock.sleep(500);
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Allows to scroll down to the top of the fragment
     */
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

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText(Comment));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Add a tag
        if (!(tagText == null)) {
        onView(withId(R.id.autocomplete_textview_in_add_item)).perform(click());
        onView(withText(tagText))
                .inRoot(isPlatformPopup())
                .perform(click());}

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());
    }

    /**
     * Adds multiple items to the inventory for testing purposes.
     *
     * @param num The number of items to add. Each number corresponds to a specific item.
     *            For example, if num = 1, it adds a Gaming Headset; if num = 2, it adds a Gaming Mouse, and so on.
     */
    private void addManyItems(int num){
        if(num>0) {
            addItem("Gaming Headset", null, "Headset for gaming", "Logitech",
                    "Astro A30", "PJF9920", "230.99", "My Headset", "Device");}
        if(num>1) {
            addItem("Gaming Mouse", null,"Mouse for gaming", "Logitech",
                    "G502 Lightspeed", "ABC123FG45", "180.00", "Cool Mouse", null);}
        if(num>2) {
            addItem("Gaming Keyboard", null, "Keyboard for gaming", "Logitech",
                    "Apex Pro", "123456FGHJ", "200.00", "Cool Keyboard", "Device");}
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

    /**
     * Adds multiple items without tags to the inventory for testing purposes.
     *
     * @param num The number of items to add. Each number corresponds to a specific item.
     *            For example, if num = 1, it adds a Gaming Headset; if num = 2, it adds a Gaming Mouse, and so on.
     */
    private void addManyItemsWithoutTag(int num) {
        if (num > 0) {
            addItem("Gaming Headset", null, "Headset for gaming", "Logitech",
                    "Astro A30", "PJF9920", "230.99", "My Headset", null);
        }
        if (num > 1) {
            addItem("Gaming Mouse", null, "Mouse for gaming", "Logitech",
                    "G502 Lightspeed", "ABC123FG45", "180.00", "Cool Mouse", null);
        }
        if (num > 2) {
            addItem("Gaming Keyboard", null, "Keyboard for gaming", "Logitech",
                    "Apex Pro", "123456FGHJ", "200.00", "Cool Keyboard", null);
        }
    }
}
