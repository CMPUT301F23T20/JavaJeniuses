package com.example.inventorymanager.ui.addItem;

import static android.app.Activity.RESULT_OK;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.ItemUtility;
import com.example.inventorymanager.ItemViewModel;
import android.Manifest;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.Item;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * The addItemFragment class is a fragment that displays a series of editable fields.
 * There are eight total fields that a user can edit: the item name, the purchase date of the item,
 * the description of the item, the make of the item, the model of the item, the serial number of the
 * item, the estimated price of the value, and a comment.
 * Once all required fields are filled the user may add the item to the item view which is displayed
 * in the home view.
 * @author Kareem Assaf, Tyler Hoekstra, Isaac Joffe, David Onchuru, Tomasz Ayobahan
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see com.example.inventorymanager.ui.viewItem.ViewItemFragment
 * @see Item
 * @see ItemViewModel
 */
public class addItemFragment extends Fragment {

    private FragmentAddItemBinding binding;
    private ArrayList<String> localImagePaths = new ArrayList<String>();
    private ArrayList<String> imageUrls = new ArrayList<String>();
    private ImageView imageView0;
    private Button addImage0Button;
    private ImageView imageView1;
    private Button addImage1Button;

    private ImageView imageView2;
    private Button addImage2Button;

    private static final int REQUEST_CODE = 22;

    /**
     * Generates the user interface of the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return the root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // get the FirebaseStorage instance
        StorageReference storageRef = itemViewModel.storage.getReference();
        StorageReference imagesRef = storageRef.child("itemImages");

        // Bind UI elements to variables
        ScrollView addItemScrollView = binding.addItemScrollView;
        EditText itemNameInput = binding.itemNameInput;
        EditText purchaseDateInput = binding.purchaseDateInput;
        EditText descriptionInput = binding.descriptionInput;
        EditText makeInput = binding.makeInput;
        EditText modelInput = binding.modelInput;
        EditText serialNumberInput = binding.serialNumberInput;
        EditText estimatedValueInput = binding.estimatedValueInput;
        EditText commentInput = binding.commentInput;
        Button addItemButton = binding.addItemButton;

        // enforcing a maximum of 3 images per item
        imageView0 = binding.itemImage0;
        imageView1 = binding.itemImage1;
        imageView2 = binding.itemImage2;

        addImage0Button = binding.addImage0Button;
        addImage1Button = binding.addImage1Button;
        addImage2Button = binding.addImage2Button;

        // Set up calendar to pop up and allow user to choose date
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
                addItemScrollView.post(() -> addItemScrollView.scrollTo(0, addItemButton.getBottom()));
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });


        // ##### ADDING IMAGE SECTION ########

        // Users are crazy, and will often try unconventional things like adding a pic to the second image placeholder before the first
        // we're gonna enforce sequential image input
        addImage1Button.setVisibility(View.GONE);
        addImage2Button.setVisibility(View.GONE);

        // when you click the respective Add Image button, navigate to the camera page
        addImage0Button.setOnClickListener( v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                },REQUEST_CODE);
            }

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);

            addImage0Button.setVisibility(View.GONE);
            addImage1Button.setVisibility(View.VISIBLE);
        });

        addImage1Button.setOnClickListener( v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                },REQUEST_CODE);
            }

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);

            addImage1Button.setVisibility(View.GONE);

            // allow user to add pic to second placeholder
            addImage2Button.setVisibility(View.VISIBLE);
        });

        addImage2Button.setOnClickListener( v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                },REQUEST_CODE);
            }

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);

            addImage2Button.setVisibility(View.GONE);
        });


        // add effect of the add button when pressed (add this item to the list)
        addItemButton.setOnClickListener(v -> {
            if (ItemUtility.validateItemFields(itemNameInput, purchaseDateInput ,descriptionInput,
                    makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput, "", itemViewModel)) {
                // Get data from input fields
                String itemName = itemNameInput.getText().toString();
                String purchaseDate = purchaseDateInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String make = makeInput.getText().toString();
                String model = modelInput.getText().toString();
                String serialNumber = serialNumberInput.getText().toString();
                String estimateValue = estimatedValueInput.getText().toString();
                String comment = commentInput.getText().toString();

                // we have to upload all the item's pictures to Firebase cloud storage before creating an item and adding that to Firestore DB
                for (int i = 0; i < this.localImagePaths.size(); i++) {

                    // fetch the path to the image
                    String localPath = this.localImagePaths.get(i);

                    // Create a unique name for each image
                    String imageName = "firebase_" + itemName + "_image" + i + ".jpg";

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
                            System.out.println(imageUrl);
                            this.imageUrls.add(imageUrl);

                            System.out.println("before printing image urls");
                            for (String url: imageUrls){
                                System.out.print(url);
                            }

                            // WARNING: this will cause a slight delay when a user adds an item because Firebase Cloud storage is asychronous
                            // We have to wait until the url for the last image has been generated before taking the user back to the home page
                            // if the image we are storing in firebase is the last image we need to store, then we create a new item with the full array of images linked to that item
                            if (finalIndex == this.localImagePaths.size() - 1) {
                                Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment, this.imageUrls);
                                // Add the new item to the shared ViewModel
                                itemViewModel.addItem(newItem);

                                // Navigate back to the home fragment
                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                navController.navigate(R.id.navigation_home);

                                ItemUtility.clearTextFields(itemNameInput, purchaseDateInput ,descriptionInput,
                                        makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);
                            }
                        });
                    }).addOnFailureListener(exception -> {
                        // If upload was unsuccessful
                        System.out.println("Upload to Firebase was unsuccessful");
                    });
                }

                // if user didn't populate the add item fields as expected
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show(); // A pop-up message to ensure validity of input
            }
        });

        return root;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            int imageCounter = localImagePaths.size(); // Get the current number of images

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

            // Update the localImagePaths list
            String imagePath = saveImageLocally(photo, "image" + imageCounter + ".jpg");
            localImagePaths.add(imagePath);

            // Enable the "Add Image" button for the current image
            currentAddImageButton.setEnabled(true);
        } else {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Helper method to save the image locally and return the path (where that image has been stored)
     * @param bitmap
     * @param fileName
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
     * Kills the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}