package com.example.inventorymanager.ui.editItem;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.inventorymanager.ImageSelectionFragment;
import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemUtility;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentEditItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * Shows the details of a single item and allows a user to edit these details.
 * Each field of the item is labelled and displayed in an editable format.
 * Users may choose to save the new details of this item or to delete this item.
 * @author Isaac Joffe, Tomasz Ayobahan, David Onchuru
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see com.example.inventorymanager.ui.viewItem.ViewItemFragment
 */
public class EditItemFragment extends Fragment {

    private FragmentEditItemBinding binding;
    private ArrayList<String> imageLinks = new ArrayList<>();

    private ImageView imageView0;
    private Button addImage0Button;
    private Button deleteImage0Button;
    private ImageView imageView1;
    private Button addImage1Button;
    private Button deleteImage1Button;

    private ImageView imageView2;
    private Button addImage2Button;
    private Button deleteImage2Button;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY_PERMISSION = 3;
    private static final int REQUEST_GALLERY = 4;

    /**
     * Provides the user interface of the fragment.
     * Receives an item label from the calling fragment and queries the database to obtain detailed information about the item.
     * Displays this item information.
     * Allows the user to edit this information in a format best suited to the data type.
     * Provides buttons for the user to choose to save the new details of this item or to delete this item.
     * @param inflater The object used to inflate views as required.
     * @param container The parent view of the fragment.
     * @param savedInstanceState The previous state of the fragment; not used in this fragment.
     * @return The root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // get the FirebaseStorage instance
        StorageReference storageRef = itemViewModel.storage.getReference();
        StorageReference imagesRef = storageRef.child("itemImages");

        // get the argument passed representing the item of interest
        String key = getArguments().getString("key");
        // fetch the full item from the database
        Item item = itemViewModel.getItem(key);

        // Bind UI elements to variables
        ScrollView editItemScrollView = binding.editItemScrollView;
        EditText itemNameInput = binding.itemNameInput;
        EditText purchaseDateInput = binding.purchaseDateInput;
        EditText descriptionInput = binding.descriptionInput;
        EditText makeInput = binding.makeInput;
        EditText modelInput = binding.modelInput;
        EditText serialNumberInput = binding.serialNumberInput;
        EditText estimatedValueInput = binding.estimatedValueInput;
        EditText commentInput = binding.commentInput;
        Button saveButton = binding.saveButton;
        Button deleteButton = binding.deleteButton;

        // enforcing a maximum of 3 images per item
        imageView0 = binding.itemImage0;
        imageView1 = binding.itemImage1;
        imageView2 = binding.itemImage2;

        addImage0Button = binding.addImage0Button;
        addImage1Button = binding.addImage1Button;
        addImage2Button = binding.addImage2Button;

        addImage0Button.setVisibility(View.GONE);
        addImage1Button.setVisibility(View.GONE);
        addImage2Button.setVisibility(View.GONE);

        deleteImage0Button = binding.deleteImage0Button;
        deleteImage1Button = binding.deleteImage1Button;
        deleteImage2Button = binding.deleteImage2Button;

        deleteImage0Button.setVisibility(View.GONE);
        deleteImage1Button.setVisibility(View.GONE);
        deleteImage2Button.setVisibility(View.GONE);

        // set the text fields to default to the text that item already has
        itemNameInput.setText(item.getItemName());
        purchaseDateInput.setText(item.getPurchaseDate());
        descriptionInput.setText(item.getDescription());
        makeInput.setText(item.getMake());
        modelInput.setText(item.getModel());
        serialNumberInput.setText(item.getSerialNumber());
        estimatedValueInput.setText(item.getEstimatedValue());
        commentInput.setText(item.getComment());

        // TODO: Convert all links to local paths
        convertUrlsToLocalPaths(item.getImageUrls());
        displayImages(this.imageLinks.size());
        System.out.println(this.imageLinks.size());
        System.out.println(this.imageLinks);

        purchaseDateInput.setOnClickListener(v -> {
            Calendar selectedDate = Calendar.getInstance(); // Create a Calendar instance for the current date
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // Set the date the user selected
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Format the date to only show month and year
                purchaseDateInput.setText(dateFormat.format(selectedDate.getTime()));
                purchaseDateInput.setError(null); // Clear any previous errors on the EditText view
            },
                    year, month, dayOfMonth
            );

            // Set the maximum date to the current date to prevent future dates
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // set up the estimated value field to only accept monetary values
            // based on code from Stack Overflow, License: Attribution-ShareAlike 4.0 International
            // published August 2016, accessed November 2023
            // https://stackoverflow.com/questions/5107901/better-way-to-format-currency-input-edittext
        estimatedValueInput.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void afterTextChanged(Editable charSequence) {
                // nothing to do
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // nothing to do
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // only need to operate if changes have been made
                if (!charSequence.toString().equals(current)) {
                    // prevent listener from activating during these changes
                    estimatedValueInput.removeTextChangedListener(this);

                    // remove dollar signs, commas, and decimals to get numerical value of string
                    String cleanString = charSequence.toString().replaceAll("[$,.]", "");
                    // convert raw number to a dollar value
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format(parsed/100);
                    // update the view on the screen to contain the properly formatted value
                    estimatedValueInput.setText(formatted);
                    estimatedValueInput.setSelection(formatted.length());

                    // changes finalized, so reactivate the listener for the future
                    estimatedValueInput.addTextChangedListener(this);
                }
            }
        });

        // Set up behavior to scroll once the commentInput EditText is filled out to scroll and reveal button
        // Close keyboard as we've reached the end of input fields
        commentInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editItemScrollView.post(() -> editItemScrollView.scrollTo(0, editItemScrollView.getBottom()));
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        // IMAGE FUNCTIONS
        // when you click the respective Add Image button, choose if you're gonna add from gallery or take a pic with camera
        addImage0Button.setOnClickListener( v -> {
            showImageOptionsDialog();
        });

        addImage1Button.setOnClickListener( v -> {
            showImageOptionsDialog();
        });

        addImage2Button.setOnClickListener( v -> {
            showImageOptionsDialog();
        });

        // User should be able to delete a picture after they have taken it but haven't submitted the "Add item" form
        deleteImage0Button.setOnClickListener( v -> {
            updateLocalImagePaths(0);
        });

        deleteImage1Button.setOnClickListener( v -> {
            updateLocalImagePaths(1);
        });

        deleteImage2Button.setOnClickListener( v -> {
            updateLocalImagePaths(2);
        });

        // add effect of the save button when pressed (save changes)
        saveButton.setOnClickListener(v -> {
            if (ItemUtility.validateItemFields(itemNameInput, purchaseDateInput ,descriptionInput,
                    makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput, key, itemViewModel)) {
                // Get data from input fields
                String itemName = itemNameInput.getText().toString();
                String purchaseDate = purchaseDateInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String make = makeInput.getText().toString();
                String model = modelInput.getText().toString();
                String serialNumber = serialNumberInput.getText().toString();
                String estimateValue = estimatedValueInput.getText().toString();
                String comment = commentInput.getText().toString();

                // We should plan to optimize this later but for the project requirements, we should be good for now
                // we have about 5GB on the cloud and 1 pic is ~250kb so thats about 100,000 pics
                // TODO after meeting requirements: Delete the existing pics from firestore to avoid clogging up our storage

                // if there are images to add
                if (this.imageLinks.size() > 0) {
                    // we have to upload all the item's pictures to Firebase cloud storage before creating an item and adding that to Firestore DB
                    ArrayList<String> tempList = new ArrayList<String>(); // A temporary list to store our generated urls
                    for (int i = 0; i < this.imageLinks.size(); i++) {

                        // fetch the path to the image
                        String localPath = this.imageLinks.get(i);

                        // Create a unique name for each image
                        String imageName = "firebase_" + itemName + "_image" + i + ".jpg";;

                        // Create a new StorageReference for each image
                        StorageReference imageRef = imagesRef.child(imageName);

                        // Create a new file from the image and Store to Firebase Cloud Storage
                        UploadTask uploadTask = imageRef.putFile(Uri.fromFile(new File(localPath)));

                        // to check which item we're waiting to send to firebase
                        int finalIndex = i;

                        // Register observers to listen for when the upload is done or if it fails
                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // If Firebase upload was successful, download the URL to the file
                            imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                                String imageUrl = downloadUrl.toString();
//                            System.out.println(imageUrl);
                                tempList.add(imageUrl);

                                System.out.println("Under save button: before printing image urls");
                                System.out.println();
                                System.out.println(imageLinks.size());
                                for (String url : imageLinks) {
                                    System.out.print(url);
                                    System.out.println();
                                }

                                // WARNING: this will cause a slight delay when a user adds an item because Firebase Cloud storage is asynchronous
                                // We have to wait until the url for the last image has been generated before taking the user back to the home page
                                // if the image we are storing in firebase is the last image we need to store, then we create a new item with the full array of images for that item
                                if (finalIndex == this.imageLinks.size() - 1) {
                                    Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment, tempList);
                                    // Add the new item to the shared ViewModel
                                    itemViewModel.editItem(key, newItem);

                                    System.out.println("Just after edit item is called");

                                    // Navigate back to the home fragment
                                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                    navController.navigate(R.id.navigation_home);

                                    ItemUtility.clearTextFields(itemNameInput, purchaseDateInput, descriptionInput,
                                            makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);
                                }
                            });
                        }).addOnFailureListener(exception -> {
                            // If upload was unsuccessful
                            System.out.println("Upload to Firebase was unsuccessful");
                        });
                    }
                }
                else {
                    Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment, null);
                    // Add the new item to the shared ViewModel
                    itemViewModel.editItem(key, newItem);

                    System.out.println("Just after edit item is called");

                    // Navigate back to the home fragment
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_home);

                    ItemUtility.clearTextFields(itemNameInput, purchaseDateInput, descriptionInput,
                            makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);
                }

            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show(); // A pop-up message to ensure validity of input
            }
        });

        // add effect of the delete button when pressed (delete this item)
        deleteButton.setOnClickListener(v -> {
            // delete this item from the database
            itemViewModel.deleteItem(key);

            // navigate back to the app home screen (item list)
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        return root;
    }

    /**
     * Rendering our images and buttons (during add and delete operations)
     * @param imageCounter The number of images you want to render
     */
    void displayImages(int imageCounter) {
        if (imageCounter == 0) {
            imageView0.setImageBitmap(null);
            addImage0Button.setVisibility(View.VISIBLE);
            deleteImage0Button.setVisibility(View.GONE);

            imageView1.setImageBitmap(null);
            addImage1Button.setVisibility(View.GONE);
            deleteImage1Button.setVisibility(View.GONE);

            imageView2.setImageBitmap(null);
            addImage2Button.setVisibility(View.GONE);
            deleteImage2Button.setVisibility(View.GONE);

        } else if (imageCounter == 1) {
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(null);
            addImage1Button.setVisibility(View.VISIBLE);
            deleteImage1Button.setVisibility(View.GONE);

            imageView2.setImageBitmap(null);
            addImage2Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

        } else if (imageCounter == 2) {
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(1)));
            addImage1Button.setVisibility(View.GONE);
            deleteImage1Button.setVisibility(View.VISIBLE);

            imageView2.setImageBitmap(null);
            addImage2Button.setVisibility(View.VISIBLE);
            deleteImage2Button.setVisibility(View.GONE);

        } else if (imageCounter == 3) {
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(1)));
            addImage1Button.setVisibility(View.GONE);
            deleteImage1Button.setVisibility(View.VISIBLE);

            imageView2.setImageBitmap(BitmapFactory.decodeFile(imageLinks.get(2)));
            addImage2Button.setVisibility(View.GONE);
            deleteImage2Button.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Sets the picture taken from the camera page to the respective ImageView
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                handleCameraResult(data);
            } else if (requestCode == REQUEST_GALLERY) {
                handleGalleryResult(data);
            }
        } else {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the result from the camera activity. Takes the selected photo and converts it to
     * Bitmap in order for it to be processed an inserted to a imageView
     *
     * @param data The Intent containing the result data from the camera activity.
     */
    private void handleCameraResult(Intent data) {
        if (data != null && data.getExtras() != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            processImageResult(photo);
        } else {
            Toast.makeText(requireContext(), "Invalid Camera Data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the result from the gallery activity. Takes the selected photo and converts it to
     * Bitmap in order for it to be processed an inserted to a imageView
     *
     * @param data The Intent containing the result data from the gallery activity.
     */
    private void handleGalleryResult(Intent data) {
        if (data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                processImageResult(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(requireContext(), "Invalid Gallery Data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Processes the result of selecting or capturing an image. Handles the flow of which
     * imageView to insert the photo and handles the saving locally of the images
     *
     * @param photo The Bitmap representing the selected or captured image.
     */
    private void processImageResult(Bitmap photo) {
        int imageCounter = this.imageLinks.size(); // Get the current number of images

        ImageView currentImageView;
        Button currentAddImageButton;

        // determine the appropriate ImageView to put our picture based on the counter
        switch (imageCounter) {
            case 0:
                currentImageView = imageView0;
                currentAddImageButton = addImage0Button;
                break;
            case 1:
                currentImageView = imageView1;
                currentAddImageButton = addImage1Button;
                break;
            case 2:
                currentImageView = imageView2;
                currentAddImageButton = addImage2Button;
                break;
            default:
                // if we have more than 3 images (not possible for now tho)
                return;
        }

        // Set the photo to the current ImageView
        currentImageView.setImageBitmap(photo);

        // Create a unique ID for each image file and Update the localImagePaths list
        String uniqueId = UUID.randomUUID().toString();
        String imagePath = saveImageLocally(photo, "image" + uniqueId + ".jpg");
        if (imageCounter < this.imageLinks.size()) {
            // Replace existing path if the counter is within the bounds
            this.imageLinks.set(imageCounter, imagePath);
        } else {
            // Otherwise, add a new path
            this.imageLinks.add(imagePath);
        }

        // Enable the "Add Image" button for the current image
        currentAddImageButton.setEnabled(true);

        // ENFORCING sequential image input
        // and accounting for the case where the user opens the camera page and cancels without actually taking the pic
        System.out.println("local image paths size: " + this.imageLinks.size());
        for (String i : imageLinks){
            System.out.print(i);
        }
        displayImages(imageLinks.size());
    }

    /**
     * Helper method to save the image locally and return the path (where that image has been stored)
     * @param bitmap The file's bitmap
     * @param fileName What you want to name the file as
     * @return
     */
    private String saveImageLocally(Bitmap bitmap, String fileName) {
        try {
            // Get the app's internal storage directory
            File internalStorageDir = requireContext().getFilesDir();

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

    /**
     * Updating our local image paths arrayList
     * @param imageToDelete
     */
    void updateLocalImagePaths(int imageToDelete){

        if (imageToDelete >= 0 && imageToDelete < this.imageLinks.size()) {
            this.imageLinks.remove(imageToDelete);
        }

        // Determine the appropriate ImageView to update based on the counter
        displayImages(this.imageLinks.size());
    }

    /**
     * Displays a dialog fragment for selecting image options, such as choosing from the gallery
     * or capturing a photo
     */
    private void showImageOptionsDialog() {
        // Obtain the fragment manager for handling fragments within this fragment
        FragmentManager fragmentManager = getChildFragmentManager();
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
    private void handleCameraIntent(){
        try {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                }, REQUEST_CAMERA_PERMISSION);
            }else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
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
    private void handleGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);

    }

    /**
     * Uses Glide API to preload all the images from their links asynchronously then calls displayImages to render them all
     * @param imageUrls
     * @return
     */
    private void convertUrlsToLocalPaths(ArrayList<String> imageUrls) {
        // Clear existing image paths
        this.imageLinks.clear();

        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);

            // Use Glide to load images asynchronously
            Glide.with(requireContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            // Process the loaded image
                            String imagePath = saveImageLocally(bitmap, "image_" + System.currentTimeMillis() + ".jpg");
                            imageLinks.add(imagePath);

                            // Display images after all are loaded
                            if (imageLinks.size() == imageUrls.size()) {
                                System.out.println("After picture download:: " + imageLinks.size());
                                displayImages(imageLinks.size());
                            }
                        }
                    });
        }
    }


    /**
     * Destroys the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
