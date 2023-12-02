package com.example.inventorymanager.ui.addTag;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.inventorymanager.Item;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.TagViewModel;
import com.example.inventorymanager.databinding.FragmentCreateTagBinding;
import java.util.ArrayList;


/**
 * A fragment for creating a new tag within the inventory management system.
 * This fragment allows the user to input a tag name and select a color for the tag.
 * It provides UI elements for the user to enter tag details and save or cancel the operation.
 *
 * @author Sumaiya Salsabil, Tomasz Ayobahan, Isaac Joffe
 * @see addTagFragment
 */
public class createTagFragment extends Fragment {
    private FragmentCreateTagBinding binding;
    private ArrayList<Item> items;
    private String tagColour;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This method initializes the fragment's layout and sets up UI elements and event listeners.
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, the fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // unpack all items sent to this fragment
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("items");
        }

        // Bind UI elements to variables
        EditText tagNameInput = binding.tagEditText;
        Button redButton = binding.redButton;
        Button blueButton = binding.blueButton;
        Button greenButton = binding.greenButton;
        Button yellowButton = binding.yellowButton;
        Button cancelButton = binding.cancelButton;
        Button saveButton = binding.saveButton;

        // default tag colour is red
        tagColour = "red";

        // change colour based on user's choice
        redButton.setOnClickListener(v -> { tagColour = "red"; });
        blueButton.setOnClickListener(v -> { tagColour = "blue"; });
        greenButton.setOnClickListener(v -> { tagColour = "green"; });
        yellowButton.setOnClickListener(v -> { tagColour = "yellow"; });

        // ViewModel to handle tag data
        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        // send bundle with the list of items
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", items);

        // Save button: Creates a new tag with the entered name and selected color
        saveButton.setOnClickListener( v -> {
            Log.d("SAVE COLOUR", tagColour);
            String tagName = tagNameInput.getText().toString();

            if (tagName.isEmpty()) {
                tagNameInput.setError("This field is required");
            } else if (tagName.length() >= 11) {
                tagNameInput.setError("Up to 10 characters");
            } else if (tagName.contains(",")) {
                tagNameInput.setError("Commas are illegal");
            } else if (tagName.contains(";")) {
                tagNameInput.setError("Semicolons are illegal");
            } else {
                tagViewModel.addTag(new Tag(tagName, tagColour));

                // Navigate to the addTagFragment after saving the tag
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.addTagFragment, bundle);
            }

        });

        // Cancel button: Navigates back to the addTagFragment without saving
        cancelButton.setOnClickListener( v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment, bundle);
        });

        return root;
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
