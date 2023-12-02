package com.example.inventorymanager;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Conducts comprehensive UI testing on the photos feature.
 * Ensures the user is able to add, view, edit and delete the photos properly.
 * Important Note: All tests should be run on Pixel 6 API 34.
 * @author Kareem Assaf, David Onchuru
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
     * Tests the add Item page (with a focus on the photos feature)
     * Creates an item and adds three photos to it
     */
    private void addPhotos(UiDevice uiDevice) {
        // Check to ensure we are on the home fragment
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));
        onView(withText("Add Tag")).check(matches(isDisplayed()));

        // Navigate to the add item fragment
        onView(withId(R.id.navigation_addItem)).perform(click());

        // Set the item's name
        onView(withId(R.id.itemNameInput)).perform(typeText("PhotoTest"));
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


        // Beginning of image tests
        onView(withId(R.id.addImage0Button)).perform(click());
        onView(withId(R.id.galleryButton)).perform(click());

        // Using UI Automator to select the Pictures folder and the first image in the gallery
        // This test assumes the user has at least one image in their gallery
        galleryPhoto(uiDevice);

        // Add a second image by taking a photo (with camera)
        onView(withId(R.id.addImage1Button)).check(matches(isDisplayed()));
        onView(withId(R.id.addImage1Button)).perform(click());
        onView(withId(R.id.takePictureButton)).perform(click());
        SystemClock.sleep(1000); // artificial delay
        cameraPermission(uiDevice); // accept permissions prompt
        onView(withId(R.id.addImage1Button)).perform(click());
        onView(withId(R.id.takePictureButton)).perform(click());
        takePicture(uiDevice);

        // Add a third image with camera
        onView(withId(R.id.addImage2Button)).check(matches(isDisplayed()));
        onView(withId(R.id.addImage2Button)).perform(click());
        onView(withId(R.id.takePictureButton)).perform(click());
        SystemClock.sleep(1000);
        takePicture(uiDevice);

        // Add the item
        onView(withId(R.id.addItemButton)).perform(click());

        SystemClock.sleep(3500);
        // Check to ensure navigation back to home fragment
        onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

        // Check to make sure there is text on screen with the item name
        onView(withText("PhotoTest")).perform(scrollTo());
        onView(withText("PhotoTest")).check(matches(isDisplayed()));
    }

    /**
     * Tests viewing images of an item functionality
     * Views the item added nd confirms it has 3 photos attached
     */
    private void viewPhotoItem (UiDevice uiDevice) {
        onView(withText("PhotoTest")).perform(scrollTo());
        SystemClock.sleep(1000);
        onView(withText("PhotoTest")).perform(click());

        try {
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // Scroll down by swiping up
            scrollable.scrollForward();

            // Check each image view field and ensure they are displayed and not null
            onView(withId(R.id.itemImage0)).check(matches(isDisplayed()));
            onView(withId(R.id.itemImage0)).check(matches(notNullValue()));
            onView(withId(R.id.itemImage1)).check(matches(isDisplayed()));
            onView(withId(R.id.itemImage1)).check(matches(notNullValue()));
            onView(withId(R.id.itemImage2)).check(matches(isDisplayed()));
            onView(withId(R.id.itemImage2)).check(matches(notNullValue()));

            // Navigate back to the home page to continue the other tests
            uiDevice.pressBack();
            SystemClock.sleep(1000);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests editing photos of an item functionality
     * The item used here was made in this.addPhotos()
     * User items are
     */
    private void editPhotoItem(UiDevice uiDevice) {
        onView(withText("PhotoTest")).perform(scrollTo());
        SystemClock.sleep(1000);
        onView(withText("PhotoTest")).perform(click());

        try {
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // Scroll down by swiping up
            scrollable.scrollForward();

            // Identify the edit button using UiSelector
            UiObject editButton = uiDevice.findObject(new UiSelector().textContains("EDIT"));
            SystemClock.sleep(1000);
            // Click on the edit button
            editButton.click();

            // Scroll to reach the images view
            scrollable.scrollForward();
            // Delete the second image
            UiObject deleteImage2Button = uiDevice.findObject(new UiSelector().resourceId("com.example.inventorymanager:id/deleteImage2Button"));
            SystemClock.sleep(1000);
            deleteImage2Button.click();
            SystemClock.sleep(1000);
            // Check to make sure you can add image2
            onView(withId(R.id.addImage2Button)).check(matches(isDisplayed()));

            // Save the edited item that now has 2 photos instead of 3
            UiObject saveButton = uiDevice.findObject(new UiSelector().textContains("SAVE"));
            SystemClock.sleep(1000);
            // Click on the delete button
            saveButton.click();

            // Check to ensure navigation back to home fragment
            SystemClock.sleep(3000);
            onView(withId(R.id.tag_button)).check(matches(isDisplayed()));

            // Check to make sure the item is there
            onView(withText("PhotoTest")).perform(scrollTo());
            onView(withText("PhotoTest")).check(matches(isDisplayed()));

            // Now must view the item to ensure there are only 2 photos
            // itemImage2 will be empty
            onView(withText("PhotoTest")).perform(click());
            scrollable.scrollForward();
            // Check to make sure all image views are full except the third one
            onView(withId(R.id.itemImage0)).check(matches(isDisplayed()));
            onView(withId(R.id.itemImage0)).check(matches(notNullValue()));
            onView(withId(R.id.itemImage1)).check(matches(isDisplayed()));
            onView(withId(R.id.itemImage1)).check(matches(notNullValue()));
            onView(withId(R.id.itemImage2)).check(matches(isDisplayed()));
            UiObject2 imageView2 = uiDevice.findObject(By.res("your.package.name:id/itemImage2"));
            Assert.assertNull("ImageView has unexpected content.", imageView2);

            uiDevice.pressBack();
            SystemClock.sleep(2000);

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the delete image of an item functionality
     * Delete a particular item in the database; specifically, the one made by addPhotos().
     */
    private void deletePhotoItem(UiDevice uiDevice) {
        onView(withText("PhotoTest")).perform(scrollTo());
        SystemClock.sleep(1000);
        onView(withText("PhotoTest")).perform(click());

        try {
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // Scroll down by swiping up
            scrollable.scrollForward();

            // Identify the delete button using UiSelector
            UiObject deleteButton = uiDevice.findObject(new UiSelector().textContains("DELETE"));
            SystemClock.sleep(1000);
            // Click on the delete button
            deleteButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to accept the "Only this time" camera permission
     * We choose this option to keep the UI tests consistent
     */
    private void cameraPermission(UiDevice uiDevice) {
        try {
            // Find the user permissions button and click it
            UiObject cameraPermission = uiDevice.findObject(new UiSelector().textContains("Only this time"));
            cameraPermission.click();
            SystemClock.sleep(1000);

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to take a picture with the camera
     */
    private void takePicture(UiDevice uiDevice) {
        try {
            UiObject shutterButton = uiDevice.findObject(new UiSelector().descriptionContains("Shutter"));
            // Get the bounds of the "Pictures" folder
            Rect bounds = shutterButton.getBounds();
            int centerX = bounds.centerX();
            int centerY = bounds.centerY();
            // Click the shutter button to take a picture
            shutterButton.click();
            SystemClock.sleep(2000);
            // Click in the same spot as the shutter button to accept the picture
            uiDevice.click(centerX, centerY);
            SystemClock.sleep(2000);
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Helper function to take a picture from the gallery.
     * Selects the first photo from the gallery
     */
    private void galleryPhoto (UiDevice uiDevice) {
        try {
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
    }

    /**
     * This is the main function for this testing class
     * Test that the user can add photos to their items
     * Tests US 04.01.01
     */
    @Test
    public void testPhotos() {
        // attempt proper login
        login();
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        addPhotos(uiDevice);    // Add a photo to test
        viewPhotoItem(uiDevice);
        editPhotoItem(uiDevice);
        deletePhotoItem(uiDevice);
    }
}
