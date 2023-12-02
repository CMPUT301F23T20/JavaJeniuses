package com.example.inventorymanager.ui.addTag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.TagAdapter;
import com.example.inventorymanager.TagViewModel;
import com.example.inventorymanager.databinding.FragmentAddTagBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Fragment used for adding tags to items within an inventory management system. This fragment provides functionalities including:
 * <ul>
 *     <li>Displaying a list of existing tags,</li>
 *     <li>Allowing the user to create new tags,</li>
 *     <li>Enabling the association of tags with items,</li>
 *     <li>Updating the display based on changes in the database.</li>
 * </ul>
 * This fragment interacts with Firebase to fetch and update tag data, ensuring that the displayed information is current.
 * It uses LiveData to observe changes in tag data and ArrayAdapter to handle the display of tags in the UI.
 * @author Sumaiya Salsabil, Tomasz Ayobahan, Isaac Joffe
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class addTagFragment extends Fragment {
    private ArrayList<Item> items;
    private String selectedItem;
    private FragmentAddTagBinding binding;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterTags;
    private static final MutableLiveData<ArrayList<Tag>> tagsLiveData = new MutableLiveData<>();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Tag selectedTag;
    private Observer<ArrayList<Tag>> dataObserver;

    /**
     * Called to have the fragment instantiate its user interface view.
     * Initializes UI components and sets up data binding and listeners.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // unpack all items sent to this fragment
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("items");
        }

        Button createTagButton = binding.createTagButton;
        createTagButton.setOnClickListener( v -> {
            // send bundle with the list of items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", items);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.createTagFragment, bundle);
        });

        autoCompleteTextView = binding.autocompleteTextview;
        // create a listener so that the tags being displayed automatically load from the database
        dataObserver = new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> updatedTags) {
                adapterTags = new TagAdapter(root.getContext(), R.layout.tag_list_item, updatedTags);
                autoCompleteTextView.setAdapter(adapterTags);
            }
        };

        // Create an instance of the shared ViewModel that manages the list of items
        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        tagViewModel.getTagsLiveData().observe(getViewLifecycleOwner(), dataObserver);

        return root;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once they know their view hierarchy has been completely created.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch the latest tags from the database
        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        tagViewModel.fetchTags();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Set a listener on the AutoCompleteTextView to handle tag selection
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                selectedTag = findTagByName(selectedItem);
            }
        });

        // Setup the button for adding tags to items
        Button addTagButton = binding.addTagButton;
        addTagButton.setOnClickListener(v -> {
            // Check if items are selected and a tag is chosen
            if (items != null && selectedItem != null) {
                // Loop through each selected item and add the chosen tag
                for (Item item : items) {
                    // update the tags string that the item currently has
                    HashMap<String, Object> mapping = item.getDocument();
                    String currentTags = (String) mapping.get("tags");
                    currentTags += selectedTag.getText() + "," + selectedTag.getColour() + ";";
                    mapping.put("tags", currentTags);
                    itemViewModel.editItem(item.getItemName(), new Item(mapping));
            }}

            // Navigate back to the home fragment after adding tags
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

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