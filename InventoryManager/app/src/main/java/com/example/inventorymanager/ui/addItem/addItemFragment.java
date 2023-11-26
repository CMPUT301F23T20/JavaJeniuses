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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.Manifest;

import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemUtility;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//import com.google.gson.Gson;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


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
    private String SCAN_MODE = "";
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
        ImageView itemImage = binding.itemImage;
        Button addImageButton = binding.addImageButton;
        Button scanDescriptionButton = binding.scanDescriptionButton;
        Button scanSerialNumberButton = binding.scanSerialNumberButton;

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

        // add effect of the scan description button when pressed (open camera and scan barcode)
        scanDescriptionButton.setOnClickListener(v -> {
            // ensure app permissions have enabled use of the camera
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            }

            // set the result listener to know that this is an item description query
            SCAN_MODE = "Description";

            // prompt the user to take a photo that will likely work for the ML models
            Toast.makeText(requireContext(), "Please take a sharp, zoomed-in, and level photo of the barcode to scan in bright lighting.", Toast.LENGTH_SHORT).show();
            // open the camera for the purpose of taking a picture
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);
        });

        // add effect of the scan serial number button when pressed (open camera and scan number)
        scanSerialNumberButton.setOnClickListener(v -> {
            // ensure app permissions have enabled use of the camera
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            }

            // set the result listener to know that this is an item description query
            SCAN_MODE = "SerialNumber";

            // prompt the user to take a photo that will likely work for the ML models
            Toast.makeText(requireContext(), "Please take a sharp, zoomed-in, and level photo of the number to read in bright lighting.", Toast.LENGTH_SHORT).show();
            // open the camera for the purpose of taking a picture
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);
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

                Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment);

                // Add the new item to the shared ViewModel
                itemViewModel.addItem(newItem);

                // Navigate back to the home fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_home);

                ItemUtility.clearTextFields(itemNameInput, purchaseDateInput ,descriptionInput,
                        makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show(); // A pop-up message to ensure validity of input
            }
        });

        return root;
    }

    /**
     * Performs operations on the image returned from the camera activity.
     * Depending on the mode of operation (a class field), different operations are undertaken.
     * @param requestCode The integer request code originally given to startActivityForResult(), allowing identification of source.
     * @param resultCode The integer result code returned by the camera activity by setResult().
     * @param data An Intent() that can return extra data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // only continue processing if it was a successful image taken
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // parse image differently depending on the mode of operation, description means lookup barcode
            if (SCAN_MODE.equals("Description")) {
                // parse the image into a format usable by the barcode detector
                Bitmap rawImage = (Bitmap) data.getExtras().get("data");
                InputImage image = InputImage.fromBitmap(rawImage, 0);

                // set up a default barcode scanner to use
                BarcodeScannerOptions options =
                    new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build();
                BarcodeScanner scanner = BarcodeScanning.getClient(options);

                // set up a task that uses the scanner to parse and analyze the image for any barcodes
                Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // check each barcode detected
                            for (int i = 0; i < barcodes.size(); i++) {
                                // these four formats are supported by the barcode lookup database
                                if ((barcodes.get(i).getFormat() == Barcode.FORMAT_UPC_A) ||
                                        (barcodes.get(i).getFormat() == Barcode.FORMAT_UPC_E) ||
                                        (barcodes.get(i).getFormat() == Barcode.FORMAT_EAN_8) ||
                                        (barcodes.get(i).getFormat() == Barcode.FORMAT_EAN_13)) {
                                    // try to fetch data for this barcode
                                    try {

                                        Toast.makeText(requireContext(), barcodes.get(i).toString(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        // display error message to the user
                                        Toast.makeText(requireContext(), "Could not fetch barcode data.", Toast.LENGTH_SHORT).show();
                                        // log details of failure
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }

                            // update the description text to match the new keywords
                            ((EditText) binding.descriptionInput).setText("Barcode scanned!");
                            // inform user of successful operation
                            Toast.makeText(requireContext(), "Description keywords automatically entered successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // display error message to the user
                            Toast.makeText(requireContext(), "Could not read barcode.", Toast.LENGTH_SHORT).show();
                            // log details of failure
                            e.printStackTrace();
                        }
                    });

            // serial number means attempt to read number from image
            } else if (SCAN_MODE.equals("SerialNumber")) {
                // parse the image into a format used by the text recognizer
                Bitmap rawImage = (Bitmap) data.getExtras().get("data");
                InputImage image = InputImage.fromBitmap(rawImage, 0);

                // for latin script, assume English language only
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                // set up a task that can parse the text in the image to extract the serial number
                Task<Text> result =
                    recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // fetch the text read and store it
                                String resultText = visionText.getText();
                                // update the description text to match the new keywords
                                ((EditText) binding.serialNumberInput).setText(resultText);
                                // inform user of successful operation
                                Toast.makeText(requireContext(), "Serial number automatically entered successfully.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // display error message to the user
                                Toast.makeText(requireContext(), "Could not read number.", Toast.LENGTH_SHORT).show();
                                // log details of failure
                                e.printStackTrace();
                            }
                        });

            // should never be encountered
            } else {
                // inform user that the operation failed
                Toast.makeText(requireContext(), "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                // log details of failure
                Log.d("DEBUG", String.format("Scan Mode: %s", SCAN_MODE));
            }

        // some failure occurred in using the camera
        } else {
            // inform user that the operation failed
            Toast.makeText(requireContext(), "Camera failure.", Toast.LENGTH_SHORT).show();
            // log details of failure
            Log.d("DEBUG", String.format("Request Code: %1$d, Result Code: %2$d", requestCode, resultCode));
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