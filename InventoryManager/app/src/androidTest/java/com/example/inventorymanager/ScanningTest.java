package com.example.inventorymanager;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;

import android.app.Instrumentation.ActivityResult;
import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.internal.platform.content.PermissionGranter;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScanningTest {
    private String SCAN_MODE = "";
    @Rule
    public ActivityScenarioRule<LoginActivity> scenario = new ActivityScenarioRule<>(LoginActivity.class);
    @Rule
    public GrantPermissionRule cameraPermissions = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    /**
     *
     */
    @Before
    public void stubCameraIntent() {
        // initialize intent for passing data
        Intents.init();

        Bitmap sampleImage = BitmapFactory.decodeFile("res\\drawable\\testing_barcode.png");
        if (SCAN_MODE == "Description") {
            sampleImage = BitmapFactory.decodeFile("..\\..\\..\\..\\..\\main\\res\\drawable\\testing_barcode.png");
        } else if (SCAN_MODE == "SerialNumber") {
            sampleImage = BitmapFactory.decodeFile("..\\..\\..\\..\\..\\main\\res\\drawable\\testing_number.png");
        } else {
            Log.d("DEBUG", "FAILURE");
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("data", (Parcelable) sampleImage);

        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);

        // provide the behavior of the camera intent
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }

    /**
     *
     */
    @After
    public void tearDown() {
        // destroy intent used to represent the camera
        Intents.release();
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
     *
     */
    private void scanDescription() {
        /////////////////////////////////////////////////////////////////
        SCAN_MODE = "Description";

        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());
        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText("Video Game"));

        // Check the purchase date by clicking the current date on the date picker and checking if it
        // matches with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateString = dateFormat.format(currentDate);
        onView(withId(R.id.purchaseDateInput)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.purchaseDateInput)).check(matches(withText(currentDateString)));

        // Set the item's description
        onView(withId(R.id.scanDescriptionButton)).perform(click());
//        onView(withId(R.id.take).perform(click());

        // Set the item's make
        onView(withId(R.id.makeInput)).perform(typeText("Nintendo"));
        onView(withId(R.id.makeInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's model
        onView(withId(R.id.modelInput)).perform(typeText("Switch Game"));
        onView(withId(R.id.modelInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.serialNumberInput)).perform(typeText("0987654321"));
        onView(withId(R.id.serialNumberInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Set the item's estimate value
        onView(withId(R.id.estimatedValueInput)).perform(typeText("89.99"));
        onView(withId(R.id.estimatedValueInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Scroll to comment input and fill it in
        onView(withId(R.id.commentInput)).perform(scrollTo());
        onView(withId(R.id.commentInput)).perform(typeText("Excellent game"));
        onView(withId(R.id.commentInput)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());

        // Check to ensure navigation back to home fragment
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("Video Game")).perform(scrollTo());
        onView(withText("Video Game")).check(matches(isDisplayed()));
    }

    /**
     *
     */
    private void deleteItem() {
        // Show the listview from Firestore
        onView(withText("Gaming Keyboard")).perform(scrollTo());
        // Click on the listview item with the text of "Car"
        onView(withText("Gaming Keyboard")).perform(click());

        // Scroll to the delete button and delete the item
        onView(withId(R.id.deleteButton)).perform(scrollTo());
        onView(withId(R.id.deleteButton)).perform(click());
    }

    /**
     *
     */
    @Test
    public void testScanDescription() {
        // login to default user
        login();
        // create an item with it's description scanned in
        scanDescription();
        // remove this item so that it can be run repeatedly
        deleteItem();
    }
}
