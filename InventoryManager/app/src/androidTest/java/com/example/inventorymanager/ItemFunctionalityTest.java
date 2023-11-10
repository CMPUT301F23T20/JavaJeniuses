package com.example.inventorymanager;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.view.KeyEvent;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Conducts comprehensive UI testing on fragments and item-related functionalities.
 * Verifies proper navigation between fragments.
 * Assesses the functionality of adding, editing, and deleting items.
 * Tests US 1.1, 1.2, 1.3, 1.4, 2.1, 2.2, and 6.1.
 * All tests should be run on Pixel 6 API 34.
 * @author Kareem Assaf, Isaac Joffe
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemFunctionalityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> scenario = new ActivityScenarioRule<>(LoginActivity.class);

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

        // verify that we are on the next page
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));
        onView(withText("Add Tag")).check(matches(isDisplayed()));
    }

    /**
     * Attempt to log in in various improper ways.
     */
    private void failLogin() {
        // attempt blank signup
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.password)).check(matches(hasErrorText("This field is required")));

        // attempt blank login
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.password)).check(matches(hasErrorText("This field is required")));

        // attempt leaving username blank
        // ensure fields are blank
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        // type in something to the password text view and close the keyboard
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("testing"));
        Espresso.closeSoftKeyboard();
        // try signing up and logging on, check error for each
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("This field is required")));

        // attempt leaving password blank
        // ensure fields are blank
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        // type in something to the username text view and close the keyboard
        onView(withId(R.id.username)).perform(click());
        onView(withId(R.id.username)).perform(typeText("testing"));
        Espresso.closeSoftKeyboard();
        // try signing up and logging on, check error for each
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("This field is required")));

        // attempt making password too short
        // ensure fields are blank
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        // type in something to the username text view and close the keyboard
        onView(withId(R.id.username)).perform(click());
        onView(withId(R.id.username)).perform(typeText("testing"));
        Espresso.closeSoftKeyboard();
        // type in something to the password text view and close the keyboard
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("12345"));
        Espresso.closeSoftKeyboard();
        // try signing up and check associated error
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password must have at least 6 characters")));

        // attempt to create account where username already exists
        // ensure fields are blank
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        // type in something to the username text view and close the keyboard
        onView(withId(R.id.username)).perform(click());
        onView(withId(R.id.username)).perform(typeText("JohnDoe"));
        Espresso.closeSoftKeyboard();
        // type in something to the password text view and close the keyboard
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("1234567"));
        Espresso.closeSoftKeyboard();
        // try signing up and check associated error
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.username)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("Username has been taken")));
        Espresso.closeSoftKeyboard();

        // attempt to log in to account with incorrect credentials
        // ensure fields are blank
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        // type in something to the username text view and close the keyboard
        onView(withId(R.id.username)).perform(click());
        onView(withId(R.id.username)).perform(typeText("JohnDoe"));
        Espresso.closeSoftKeyboard();
        // type in something to the password text view and close the keyboard
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("1234567"));
        Espresso.closeSoftKeyboard();
        // try logging in and check associated errors
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.username)).check(matches(hasErrorText("Incorrect username or password")));
        onView(withId(R.id.password)).check(matches(hasErrorText("Incorrect username or password")));
    }

    /**
     * Add a simple sample item to the database.
     */
    private void addItem() {
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
    }

    /**
     * Attempt to add an item with incorrect fields in various ways.
     */
    private void failAdd() {
        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        scrollToBottomAdd();

        // Check the item name field with an empty input
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
        onView(withId(R.id.itemNameInput)).check(matches(hasErrorText("This field is required")));

        // Check the item name field with too many characters
        onView(withId(R.id.itemNameInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
        onView(withId(R.id.itemNameInput)).perform(click());
        onView(withId(R.id.itemNameInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.itemNameInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the date field with an empty field
        scrollToBottomAdd();
        onView(withId(R.id.addPurchaseDateTextView)).perform(scrollTo());
        onView(withId(R.id.purchaseDateInput)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());

        // Check the description field with too many characters (optional field)
        onView(withId(R.id.descriptionInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addDescriptionTextView)).perform(scrollTo());
        onView(withId(R.id.descriptionInput)).perform(click());
        onView(withId(R.id.descriptionInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.descriptionInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the make field with an empty input
        scrollToBottomAdd();
        onView(withId(R.id.addMakeTextView)).perform(scrollTo());
        onView(withId(R.id.makeInput)).perform(click());
        onView(withId(R.id.makeInput)).check(matches(hasErrorText("This field is required")));

        // Check the make field with too many characters
        onView(withId(R.id.makeInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addMakeTextView)).perform(scrollTo());
        onView(withId(R.id.makeInput)).perform(click());
        onView(withId(R.id.makeInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.makeInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the model field with an empty input
        scrollToBottomAdd();
        onView(withId(R.id.addModelTextView)).perform(scrollTo());
        onView(withId(R.id.modelInput)).perform(click());
        onView(withId(R.id.modelInput)).check(matches(hasErrorText("This field is required")));

        // Check the model field with too many characters
        onView(withId(R.id.modelInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addModelTextView)).perform(scrollTo());
        onView(withId(R.id.modelInput)).perform(click());
        onView(withId(R.id.modelInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.modelInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the serial number field with too many characters (optional field)
        onView(withId(R.id.serialNumberInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addSerialNumberTextView)).perform(scrollTo());
        onView(withId(R.id.serialNumberInput)).perform(click());
        onView(withId(R.id.serialNumberInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.serialNumberInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the estimate value field with an empty input
        scrollToBottomAdd();
        onView(withId(R.id.addEstimatedValueTextView)).perform(scrollTo());
        onView(withId(R.id.estimatedValueInput)).perform(click());
        onView(withId(R.id.estimatedValueInput)).check(matches(hasErrorText("This field is required")));

        // Check the comment field with too many characters (optional field)
        onView(withId(R.id.commentInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomAdd();
        onView(withId(R.id.addCommentTextView)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(click());
        onView(withId(R.id.commentInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.commentInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Scroll back to the top of add item fragment
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
    }

    /**
     * Edit an existing item in the database, specifically the one made by addItem().
     */
    private void editItem() {
        // Scroll and click the item to edit
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).perform(click());

        // click the edit button to edit this item
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
    }

    /**
     * Attempt to edit an item with incorrect fields in various ways.
     */
    private void failEdit() {
        // Scroll and click the item to edit
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        onView(withText("Gaming Keyboard")).perform(click());

        // click the edit button to edit this item
        onView(withId(R.id.editButton)).perform(scrollTo());
        onView(withId(R.id.editButton)).perform(click());

        // clear all fields to test blank input
        onView(withId(R.id.itemNameInput)).perform(clearText());
        onView(withId(R.id.purchaseDateInput)).perform(clearText());
        onView(withId(R.id.descriptionInput)).perform(clearText());
        onView(withId(R.id.makeInput)).perform(clearText());
        onView(withId(R.id.modelInput)).perform(clearText());
        onView(withId(R.id.serialNumberInput)).perform(clearText());
        onView(withId(R.id.estimatedValueInput)).perform(clearText());
        onView(withId(R.id.commentInput)).perform(clearText());
        scrollToBottomEdit();

        // Check the item name field with an empty input
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
        onView(withId(R.id.itemNameInput)).check(matches(hasErrorText("This field is required")));

        // Check the item name field with too many characters
        onView(withId(R.id.itemNameInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
        onView(withId(R.id.itemNameInput)).perform(click());
        onView(withId(R.id.itemNameInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.itemNameInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the date field with an empty field
        scrollToBottomEdit();
        onView(withId(R.id.addPurchaseDateTextView)).perform(scrollTo());
        onView(withId(R.id.purchaseDateInput)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());

        // Check the description field with too many characters (optional field)
        onView(withId(R.id.descriptionInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addDescriptionTextView)).perform(scrollTo());
        onView(withId(R.id.descriptionInput)).perform(click());
        onView(withId(R.id.descriptionInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.descriptionInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the make field with an empty input
        scrollToBottomEdit();
        onView(withId(R.id.addMakeTextView)).perform(scrollTo());
        onView(withId(R.id.makeInput)).perform(click());
        onView(withId(R.id.makeInput)).check(matches(hasErrorText("This field is required")));

        // Check the make field with too many characters
        onView(withId(R.id.makeInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addMakeTextView)).perform(scrollTo());
        onView(withId(R.id.makeInput)).perform(click());
        onView(withId(R.id.makeInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.makeInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the model field with an empty input
        scrollToBottomEdit();
        onView(withId(R.id.addModelTextView)).perform(scrollTo());
        onView(withId(R.id.modelInput)).perform(click());
        onView(withId(R.id.modelInput)).check(matches(hasErrorText("This field is required")));

        // Check the model field with too many characters
        onView(withId(R.id.modelInput)).perform(typeText("A lot of characters........"));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addModelTextView)).perform(scrollTo());
        onView(withId(R.id.modelInput)).perform(click());
        onView(withId(R.id.modelInput)).check(matches(hasErrorText("Up to 15 characters")));
        onView(withId(R.id.modelInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the serial number field with too many characters (optional field)
        onView(withId(R.id.serialNumberInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addSerialNumberTextView)).perform(scrollTo());
        onView(withId(R.id.serialNumberInput)).perform(click());
        onView(withId(R.id.serialNumberInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.serialNumberInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // Check the estimate value field with an empty input
        scrollToBottomEdit();
        onView(withId(R.id.addEstimatedValueTextView)).perform(scrollTo());
        onView(withId(R.id.estimatedValueInput)).perform(click());
        onView(withId(R.id.estimatedValueInput)).check(matches(hasErrorText("This field is required")));


        // Check the comment field with too many characters (optional field)
        onView(withId(R.id.commentInput)).perform(typeText("A lot of characters..............."));
        Espresso.closeSoftKeyboard();
        scrollToBottomEdit();
        onView(withId(R.id.addCommentTextView)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(click());
        onView(withId(R.id.commentInput)).check(matches(hasErrorText("Up to 20 characters")));
        onView(withId(R.id.commentInput)).perform(clearText());
        Espresso.closeSoftKeyboard();

        // set the original values back for each field to leave item unchanged
        onView(withId(R.id.addItemNameTextView)).perform(scrollTo());
        onView(withId(R.id.itemNameInput)).perform(typeText("Gaming Keyboard"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateString = dateFormat.format(currentDate);
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.purchaseDateInput)).check(matches(withText(currentDateString)));
        onView(withId(R.id.descriptionInput)).perform(typeText("Keyboard for gaming"));
        onView(withId(R.id.descriptionInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.makeInput)).perform(typeText("Logitech"));
        onView(withId(R.id.makeInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.modelInput)).perform(typeText("Apex Pro"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.serialNumberInput)).perform(typeText("1A2B3C4D5E"));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.estimatedValueInput)).perform(typeText("200.00"));
        onView(withId(R.id.estimatedValueInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText("Nice keyboard"));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        // save these changes, which should be nothing
        onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Delete a particular item in the database; specifically, the one made by addItem().
     */
    private void deleteItem1() {
        // Show the listview from Firestore
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText("Gaming Keyboard")).perform(click());

        // Scroll to the delete button and delete the item
        onView(withId(R.id.deleteButton)).perform(scrollTo());
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Delete a particular item in the database; specifically, the one made by editItem().
     */
    private void deleteItem2() {
        // Show the listview from Firestore
        onView(withText("Car")).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText("Car")).perform(click());

        // Scroll to the delete button and delete the item
        onView(withId(R.id.deleteButton)).perform(scrollTo());
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     * Allows to scroll down in the add item fragment for testing.
     */
    private void scrollToBottomAdd() {
        // scroll to the comment field and open and close the keyboard
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(click());
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        // click on the add item button which is now shown
        onView(withId(R.id.addItemButton)).perform(click());
    }

    /**
     * Allows to scroll down in the edit item fragment for testing.
     */
    private void scrollToBottomEdit() {
        // scroll to the comment field and open and close the keyboard
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(click());
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        // click on the save changes button which is now shown
        onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Test that the uer can login to the application properly.
     * Tests US 6.1.
     */
    @Test
    public void testLogin() {
        // attempt proper login
        login();
    }

    /**
     * Test that each sign up and log in error message appears correctly.
     * Tests US 6.1.
     */
    @Test
    public void testFailedLogin() {
        // attempt improper login
        failLogin();
        // attempt proper login
        login();
    }

    /**
     * Test the navigation to the "Add Item" fragment and adding an item.
     * Tests US 1.1, 2.1, and 2.2.
     */
    @Test
    public void testAddItem() {
        // login to default user
        login();
        // check that estimated value field starts as empty
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add item to the list properly
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // remove item for future tests
        deleteItem1();
        // check that estimated value field returns to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }

    /**
     * Test the input fields for the "Add Item" fragment and ensures the correct error messages are showing.
     * Tests US 1.1, 2.1, and 2.2.
     */
    @Test
    public void testFailAddItem(){
        // login to default user
        login();
        // check that estimated value field starts as empty
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add item to the list incorrectly
        failAdd();
        // add item to the list properly
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // remove item for future tests
        deleteItem1();
        // check that estimated value field returns to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }


    /**
     * Test the navigation to edit an item and editing an item
     * In order to edit an item, you must navigate to view item so this function also tests that.
     * Tests US 1.2, 1.3, 2.1, and 2.2.
     */
    @Test
    public void testEditItem() {
        // login to default user
        login();
        // check that estimated value field starts with last item
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add a new item
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // edit the existing item
        editItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$5,000.00")));
        // delete the new item
        deleteItem2();
        // check that estimated value field return to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }

    /**
     * Test the input fields for the "Edit Item" fragment and ensures the correct error messages are showing.
     * Tests US 1.2, 1.3, 2.1, and 2.2.
     */
    @Test
    public void testFailEditItem() {
        // login to default user
        login();
        // check that estimated value field starts with last item
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add a new item
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // edit existing item incorrectly
        failEdit();
        // check that estimated value field remains constant
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // edit the existing item
        editItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$5,000.00")));
        // delete the new item
        deleteItem2();
        // check that estimated value field return to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }

    /**
     * Test the navigation to delete an item and deleting an item.
     * In order to delete an item, you must navigate to view item so this function also tests that.
     * Tests US 1.2, 1.4, 2.1, and 2.2.
     */
    @Test
    public void testDeleteItem() {
        // login to default user
        login();
        // check that estimated value field starts as empty
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add item to the list
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // remove item for future tests
        deleteItem1();
        // check that estimated value field return to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
        // add item to the list
        addItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$200.00")));
        // edit the existing item
        editItem();
        // check that estimated value field displays updated value
        onView(withId(R.id.total_value)).check(matches(withText("$5,000.00")));
        // delete the new item
        deleteItem2();
        // check that estimated value field return to nothing
        onView(withId(R.id.total_value)).check(matches(withText("$0.00")));
    }
}
