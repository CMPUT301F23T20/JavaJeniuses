package com.example.inventorymanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * This is a utility class that contains helpful methods for handling image-related operations and validations.
 * @author Kareem Assaf, David Onchuru
 * @see com.example.inventorymanager.ui.addItem.addItemFragment
 * @see com.example.inventorymanager.ui.editItem.EditItemFragment
 */
public class ImageUtility {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY = 3;
    private final Fragment fragment;
    public ImageUtility(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Displays a dialog fragment for selecting image options, such as choosing from the gallery
     * or capturing a photo
     */
    public void showImageOptionsDialog() {
        // Obtain the fragment manager for handling fragments within this fragment
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        // Create an instance of ImageSelectionFragment, which provides the image selection options
        ImageSelectionFragment imageOptionsFragment = new ImageSelectionFragment();

        imageOptionsFragment.setOnImageOptionClickListener(new ImageSelectionFragment.OnImageOptionClickListener() {
            @Override
            public void onOptionClick(int choice) {
                // Handle the user's choice based on the selected option
                if (choice == 1){
                    handleGalleryIntent();

                } else if (choice == 2){
                    handleCameraIntent();
                }
            }
        });

        imageOptionsFragment.show(fragmentManager, "ImageOptionsFragment");
    }

    /**
     * Handles the camera intent by checking for CAMERA permission, requesting it if necessary,
     * and launching the camera application to capture an image.
     */
    public void handleCameraIntent(){
        try {
            if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                }, REQUEST_CAMERA_PERMISSION);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fragment.startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }

        } catch (SecurityException e) {
            // Handle the exception, e.g., request the permission or show a message to the user.
            e.printStackTrace(); // Log the exception for debugging purposes.
        }
    }

    /**
     * Handles the gallery intent by launching the gallery to allow the user to select from the
     * photo gallery
     */
    public void handleGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        fragment.startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    /**
     * Helper method to save the image locally and return the path (where that image has been stored)
     * @param bitmap The file's bitmap
     * @param fileName What you want to name the file as
     * @return
     */
    public String saveImageLocally(Bitmap bitmap, String fileName) {
        try {
            // Get the app's internal storage directory
            File internalStorageDir = fragment.requireContext().getFilesDir();

            // Create a directory named "images" if it doesn't exist
            File imagesDir = new File(internalStorageDir, "images");
            if (!imagesDir.exists()) {
                imagesDir.mkdir();
            }

            // Create a File object for the image file
            File imageFile = new File(imagesDir, fileName);

            // Create a FileOutputStream to write the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            // Compress the bitmap and write it to the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // Flush and close the stream
            outputStream.flush();
            outputStream.close();

            // Return the absolute path to the saved image file so that we can display it on the add item page that the user is still working on
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string if there's an error
        }
    }
}

