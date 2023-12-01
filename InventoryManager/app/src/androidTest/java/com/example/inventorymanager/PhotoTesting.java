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

import android.graphics.Rect;
import android.os.SystemClock;
import android.view.KeyEvent;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

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
public class PhotoTesting {

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

    private void addPhoto() {
        // Check to ensure we are on the home fragment

        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));
        onView(withText("Add Tag")).check(matches(isDisplayed()));

        onView(withId(R.id.navigation_addItem)).perform(click());

        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText("Add Photo Test"));


        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());

        // Set the item's description
        onView(withId(R.id.descriptionInput)).perform(typeText("Testing adding photos"));
        onView(withId(R.id.descriptionInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's make
        onView(withId(R.id.makeInput)).perform(typeText("No make"));
        onView(withId(R.id.makeInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's model
        onView(withId(R.id.modelInput)).perform(typeText("No model"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.serialNumberInput)).perform(typeText("1A2B3C4D5E"));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's estimate value
        onView(withId(R.id.estimatedValueInput)).perform(typeText("100.00"));
        Espresso.closeSoftKeyboard();

        // Set the item's comment
        onView(withId(R.id.commentInput)).perform(typeText("No comment"));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.addImage0Button)).perform(click());
        onView(withId(R.id.galleryButton)).perform(click());

        // Using UI Automator to select the Pictures folder and the first image in the gallery
        // This test assumes the user has at least one image in their gallery
        try {

            UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            // Find the pictures folder
            UiObject picturesFolder = uiDevice.findObject(new UiSelector().textContains("Pictures"));
            // Get the bounds of the "Pictures" folder
            Rect bounds = picturesFolder.getBounds();

            int centerY = bounds.centerY();

            // Perform the click at the coordinates of the "Pictures" folder
            uiDevice.click(0, centerY);
            // Introduce a pause in between clicks
            SystemClock.sleep(2000);
            // In order to click at the first image in the gallery, we must click at the same position
            // as we clicked for the picture folder
            uiDevice.click(0, centerY);

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        // Check to ensure we can add another image
        onView(withId(R.id.addImage1Button)).check(matches(isDisplayed()));


    }


    /**
     * Test that the user can add photos to their items
     * Tests US 4.1
     */
    @Test
    public void testAddPhoto() {
        // attempt proper login
        login();
        addPhoto();
    }

}
