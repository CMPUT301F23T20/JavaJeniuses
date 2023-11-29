package com.example.inventorymanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


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
    private Fragment fragment;

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
            }else {
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


}

