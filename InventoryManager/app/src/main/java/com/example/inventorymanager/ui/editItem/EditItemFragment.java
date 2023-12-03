package com.example.inventorymanager.ui.editItem;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import static android.app.Activity.RESULT_OK;
import androidx.annotation.Nullable;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.inventorymanager.ImageUtility;
import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemUtility;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.TagAdapter;
import com.example.inventorymanager.TagViewModel;
import com.example.inventorymanager.databinding.FragmentEditItemBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


/**
 * Shows the details of a single item and allows a user to edit these details.
 * Each field of the item is labelled and displayed in an editable format.
 * Users may choose to save the new details of this item or to delete this item.
 * @author Isaac Joffe, Tomasz Ayobahan, David Onchuru, Sumaiya Salsabil
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see com.example.inventorymanager.ui.viewItem.ViewItemFragment
 */
public class EditItemFragment extends Fragment {
    private FragmentEditItemBinding binding;
    private final ArrayList<String> imagePaths = new ArrayList<>(); // local paths
    private final ArrayList<String> tempList = new ArrayList<>(); // A temporary list to store our generated urls
    private ImageUtility imageUtility;
    private ImageView imageView0;
    private Button addImage0Button;
    private Button deleteImage0Button;
    private ImageView imageView1;
    private Button addImage1Button;
    private Button deleteImage1Button;
    private ImageView imageView2;
    private Button addImage2Button;
    private Button deleteImage2Button;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY = 3;
    private String SCAN_MODE = "";
    private static final int REQUEST_CODE = 22;
    private ArrayAdapter<String> adapterTags, itemTagsAdapter;
    private Observer<ArrayList<Tag>> dataObserver;
    private String selectedItemAdd, selectedItemDelete;
    private Tag selectedTagAdd, selectedTagDelete;

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

        // get the Firebase Storage instance
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
        AutoCompleteTextView addTagAutoCompleteTextView = binding.autocompleteTextviewInEditItemAddTag;
        AutoCompleteTextView deleteTagAutoCompleteTextView = binding.autocompleteTextviewInEditItemDeleteTag;
        Button saveButton = binding.saveButton;
        Button deleteButton = binding.deleteButton;
        Button scanDescriptionButton = binding.scanDescriptionButton;
        Button scanSerialNumberButton = binding.scanSerialNumberButton;

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

        convertUrlsToLocalPaths(item.getImageUrls());
        // render the item's images
        displayImages(this.imagePaths.size());
        Log.d("DEBUG", "Entering edit fragment" + this.imagePaths.size());

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
            private final String current = "";
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

        // create a listener so that the tags being displayed automatically load from the database
        dataObserver = new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> updatedTags) {
                adapterTags = new TagAdapter(root.getContext(), R.layout.tag_list_item, updatedTags);
                addTagAutoCompleteTextView.setAdapter(adapterTags);
            }
        };

        // Create an instance of the shared ViewModel that manages the list of items
        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        tagViewModel.getTagsLiveData().observe(getViewLifecycleOwner(), dataObserver);

        // Set a listener on the Add Tag AutoCompleteTextView to handle tag selection
        addTagAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemAdd = parent.getItemAtPosition(position).toString();
                selectedTagAdd = findTagByName(selectedItemAdd);
            }
        });

        // display the item's tags under delete dropdown
        itemTagsAdapter = new TagAdapter(root.getContext(), R.layout.tag_list_item, item.getTagsArray());
        deleteTagAutoCompleteTextView.setAdapter(itemTagsAdapter);

        // Set a listener on the Delete Tag AutoCompleteTextView to handle tag selection
        deleteTagAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemDelete = parent.getItemAtPosition(position).toString();
                selectedTagDelete = findTagByName(selectedItemDelete);
            }
        });


        // IMAGE FUNCTIONALITY
        // when you click the respective Add Image button, choose if you're gonna add from gallery or take a pic with camera
        imageUtility = new ImageUtility(this);
        addImage0Button.setOnClickListener(v -> imageUtility.showImageOptionsDialog());
        addImage1Button.setOnClickListener(v -> imageUtility.showImageOptionsDialog());
        addImage2Button.setOnClickListener(v -> imageUtility.showImageOptionsDialog());

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

                // if there are images to add
                if (this.imagePaths.size() > 0) {
                    // we have to upload all the item's pictures to Firebase cloud storage before creating an item and editing that in Firestore DB

                    // store a copy of all our uploadTasks
                    // will use to confirm if all pics have been uploaded successfully
                    List<Task<Uri>> uploadTasks = new ArrayList<>();

                    Log.d("DEBUG", "line 293, image paths size:::: " + imagePaths.size());
                    for (int i = 0; i < this.imagePaths.size(); i++) {
                        // fetch the path to the image
                        String localPath = this.imagePaths.get(i);
                        // Create a unique name for each image
                        String imageName = "firebase_" + itemName + "_image" + i + ".jpg";

                        // Create a new StorageReference for each image
                        StorageReference imageRef = imagesRef.child(imageName);
                        UploadTask uploadTask = imageRef.putFile(Uri.fromFile(new File(localPath)));

                        // Register the task to the list
                        uploadTasks.add(uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return imageRef.getDownloadUrl();
                        }));
                    }

                    // Wait for all tasks to complete
                    // WARNING: this will cause a slight delay when a user adds an item because Firebase Cloud storage is asynchronous
                    // We have to wait until the url for the last image has been generated before taking the user back to the home page
                    // if the image we are storing in firebase is the last image we need to store, then we create a new item with the full array of images for that item
                    Tasks.whenAllSuccess(uploadTasks).addOnSuccessListener(results -> {
                        // Convert List<Uri> to ArrayList<String> (Our default data structure for imageUrls)
                        ArrayList<String> imageUrls = new ArrayList<>();
                        for (Object uri : results) {
                            imageUrls.add(uri.toString());
                        }

                        Log.d("DEBUG", "line 326, url size:::: " + imageUrls.size());

                        String tag = item.getTags();
                        // delete selected tag
                        if (selectedTagDelete != null && !selectedTagDelete.equals("")) {
                            String tagToDelete = selectedTagDelete.getText() + "," + selectedTagDelete.getColour() + ";";
                            tag = tag.replace(tagToDelete, "");
                        }
                        // add selected tag
                        if (selectedTagAdd != null && !selectedTagAdd.equals("")) {
                            tag += selectedTagAdd.getText() + "," + selectedTagAdd.getColour() + ";";
                        }

                        Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment, tag, imageUrls);
                        itemViewModel.editItem(key, newItem);

                        // Navigate back to the home fragment
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                        navController.navigate(R.id.navigation_home);

                        ItemUtility.clearTextFields(itemNameInput, purchaseDateInput, descriptionInput,
                                makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);

                    }).addOnFailureListener(exception -> {
                        // Handle failure
                        Log.d("DEBUG", "One or more image uploads failed");
                    });
                }

                // if there are no pics to add
                else {
                    String tag = item.getTags();
                    // delete selected tag
                    if (selectedTagDelete != null && !selectedTagDelete.equals("")) {
                        String tagToDelete = selectedTagDelete.getText() + "," + selectedTagDelete.getColour() + ";";
                        tag = tag.replace(tagToDelete, "");
                    }
                    // add selected tag
                    if (selectedTagAdd != null && !selectedTagAdd.equals("")) {
                        tag += selectedTagAdd.getText() + "," + selectedTagAdd.getColour() + ";";
                    }

                    Item newItem = new Item(itemName, purchaseDate, description, model, make, serialNumber, estimateValue, comment, tag, null);
                    // Add the new item to the shared ViewModel
                    itemViewModel.editItem(key, newItem);

                    Log.d("DEBUG", "Just after edit item is called");

                    // Navigate back to the home fragment
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_home);

                    ItemUtility.clearTextFields(itemNameInput, purchaseDateInput, descriptionInput,
                            makeInput, modelInput, serialNumberInput, estimatedValueInput, commentInput);
                }

            // if user didn't populate the add item fields as expected
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
     * Performs operations on the image returned from the camera activity.
     * Depending on the mode of operation (a class field), different operations are undertaken.
     * @param requestCode The integer request code originally given to startActivityForResult(), allowing identification of source.
     * @param resultCode The integer result code returned by the camera activity by setResult().
     * @param data An Intent() that can return extra data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // only continue processing if it was a successful image taken
        if (resultCode == RESULT_OK) {
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
                                // ensure that barcodes are found before proceeding in analysis
                                if (barcodes.size() == 0) {
                                    // display error message to the user
                                    Toast.makeText(requireContext(), "Could not read barcode.", Toast.LENGTH_SHORT).show();
                                    return;    // no use proceeding
                                }

                                // check each barcode detected
                                for (int i = 0; i < barcodes.size(); i++) {
                                    // these four formats are supported by the barcode lookup database
                                    if ((barcodes.get(i).getFormat() == Barcode.FORMAT_UPC_A) ||
                                            (barcodes.get(i).getFormat() == Barcode.FORMAT_UPC_E) ||
                                            (barcodes.get(i).getFormat() == Barcode.FORMAT_EAN_8) ||
                                            (barcodes.get(i).getFormat() == Barcode.FORMAT_EAN_13)) {
                                        // try to fetch data for this barcode
                                        try {
                                            // format the database search query for barcode and API key
                                            String API_KEY = "kzazmbk749ke6jghx29bnn68yp6kwo";
                                            String query = String.format(
                                                    "https://api.barcodelookup.com/v3/products?barcode=%1$s&formatted=y&key=%2$s",
                                                    barcodes.get(i).getRawValue(),
                                                    API_KEY);
                                            URL url = new URL(query);

                                            // ensure that network calls are allowed to be made in the main thread
                                            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                                            // read the response to this search query
                                            BufferedReader searchResultsReader = new BufferedReader(new InputStreamReader(url.openStream()));
                                            String nextLine = "";    // next line of data to fetch
                                            StringBuilder searchResultsBuilder = new StringBuilder();    // total cumulative data
                                            // read in all response lines until no data is left
                                            while ((nextLine = searchResultsReader.readLine()) != null) {
                                                // track each new line
                                                searchResultsBuilder.append(nextLine);
                                            }
                                            String searchResults = searchResultsBuilder.toString();

                                            // parse the JSON object returned from the API
                                            JSONObject originalJsonObject = new JSONObject(searchResults);
                                            // retrieve the array of products, which is all that is inside the original objects
                                            JSONArray jsonArray = originalJsonObject.getJSONArray("products");
                                            // only the first object in this array matters (usually length 1 anyways)
                                            JSONObject mainJsonObject = jsonArray.getJSONObject(0);
                                            // fetch relevant information about the object to form description
                                            String description = mainJsonObject.get("title").toString();
                                            // trim string so it can fit inside the description field
                                            if (description.length() > 40) {
                                                description = description.substring(0, 40);
                                            }

                                            // update the description text to match the new keywords
                                            binding.descriptionInput.setText(description);
                                            // inform user of successful operation
                                            Toast.makeText(requireContext(), "Description keywords automatically entered successfully.", Toast.LENGTH_SHORT).show();

                                            // inform the users if any issue arises that prevent data from being automatically parsed
                                        } catch (Exception e) {
                                            // display error message to the user
                                            Toast.makeText(requireContext(), "Could not fetch barcode data.", Toast.LENGTH_SHORT).show();
                                            // log details of failure
                                            e.printStackTrace();
                                        }
                                        break;    // inform the users if any issue arises that prevent data from being automatically parsed
                                    }
                                }
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
                                        // trim string so it can fit inside the field and skip other lines
                                        if (resultText.contains("\n")) {
                                            resultText = resultText.substring(0, resultText.indexOf("\n"));
                                        }
                                        if (resultText.length() > 20) {
                                            resultText = resultText.substring(0, 20);
                                        }
                                        // update the description text to match the new keywords
                                        binding.serialNumberInput.setText(resultText);
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

            // handle image upload request
            } else if (requestCode == REQUEST_CAMERA) {
                handleCameraResult(data);

            // handle image gallery upload request
            } else if (requestCode == REQUEST_GALLERY) {
                handleGalleryResult(data);

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

        SCAN_MODE = "";
    }

    /**
     * This method renders our images and buttons (during add and delete operations).
     * @param imageCounter The number of images you want to render.
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
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(null);
            addImage1Button.setVisibility(View.VISIBLE);
            deleteImage1Button.setVisibility(View.GONE);

            imageView2.setImageBitmap(null);
            addImage2Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

        } else if (imageCounter == 2) {
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(1)));
            addImage1Button.setVisibility(View.GONE);
            deleteImage1Button.setVisibility(View.VISIBLE);

            imageView2.setImageBitmap(null);
            addImage2Button.setVisibility(View.VISIBLE);
            deleteImage2Button.setVisibility(View.GONE);

        } else if (imageCounter == 3) {
            imageView0.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(0)));
            addImage0Button.setVisibility(View.GONE);
            deleteImage0Button.setVisibility(View.VISIBLE);

            imageView1.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(1)));
            addImage1Button.setVisibility(View.GONE);
            deleteImage1Button.setVisibility(View.VISIBLE);

            imageView2.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(2)));
            addImage2Button.setVisibility(View.GONE);
            deleteImage2Button.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles the result from the camera activity. Takes the selected photo and converts it to
     * Bitmap in order for it to be processed an inserted to a imageView.
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
     * Bitmap in order for it to be processed an inserted to a imageView.
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
        int imageCounter = this.imagePaths.size(); // Get the current number of images

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
        String imagePath = imageUtility.saveImageLocally(photo, "image" + uniqueId + ".jpg");

        if (imageCounter < this.imagePaths.size()) {
            // Replace existing path if the counter is within the bounds
            this.imagePaths.set(imageCounter, imagePath);
        } else {
            // Otherwise, add a new path
            this.imagePaths.add(imagePath);
        }

        // Enable the "Add Image" button for the current image
        currentAddImageButton.setEnabled(true);

        // ENFORCING sequential image input
        // and accounting for the case where the user opens the camera page and cancels without actually taking the pic
        System.out.println("local image paths size: " + this.imagePaths.size());
        for (String i : imagePaths){
            System.out.print(i);
        }
        displayImages(imagePaths.size());
    }

    /**
     * Updating our local image paths arrayList
     * @param imageToDelete
     */
    void updateLocalImagePaths(int imageToDelete){
        if (imageToDelete >= 0 && imageToDelete < this.imagePaths.size()) {
            this.imagePaths.remove(imageToDelete);
        }

        // Determine the appropriate ImageView to update based on the counter
        displayImages(this.imagePaths.size());
    }

    /**
     * Uses Glide API to preload all the images from their links asynchronously then calls displayImages to render them all
     * @param imageUrls
     */
    private void convertUrlsToLocalPaths(ArrayList<String> imageUrls) {
        // Clear existing image paths
        this.imagePaths.clear();

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
                        String imagePath = imageUtility.saveImageLocally(bitmap, "image_" + System.currentTimeMillis() + ".jpg");
                        imagePaths.add(imagePath);

                        // Display images after all are loaded
                        if (imagePaths.size() == imageUrls.size()) {
                            System.out.println("After picture download:: " + imagePaths.size());
                            displayImages(imagePaths.size());
                        }
                    }
                });
        }
    }

    /**
     * Finds a tag by its name.
     * @param tagName The name of the tag to find.
     * @return The Tag object if found, or null otherwise.
     */
    private Tag findTagByName(String tagName) {
        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        ArrayList<Tag> myTags = tagViewModel.getTagsLiveData().getValue();
        for (Tag tag : myTags) {
            if (tag.getText().equals(tagName)) {
                return tag;
            }
        }
        return null; // Return null if tag not found
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
