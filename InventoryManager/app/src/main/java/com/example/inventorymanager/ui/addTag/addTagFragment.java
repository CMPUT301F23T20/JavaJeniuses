package com.example.inventorymanager.ui.addTag;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.databinding.FragmentAddTagBinding;
import com.example.inventorymanager.databinding.FragmentSortOptionsBinding;
import com.example.inventorymanager.ui.filter.filteredItemsFragment;
import com.example.inventorymanager.ui.sort.sortedItemsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addTagFragment extends Fragment {

    private ArrayList<Item> items;
    private ItemViewModel itemViewModel;
    private String tagName, tagColour;
    private ArrayList<String> tagInfo, tagItems;
    private String selectedItem;
    private FragmentAddTagBinding binding;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private ArrayList<Tag> allTags = new ArrayList<>();
    private Tag selectedTag;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentAddTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // unpack all items sent to this fragment
        items = getArguments().getParcelableArrayList("items");

        Button createTagButton = binding.createTagButton;

        createTagButton.setOnClickListener( v -> {

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.createTagFragment);
        });

        tagItems = new ArrayList<>();
        Tag tag1 = new Tag("Tag 1", "red");
        Tag tag2 = new Tag("Tag 2", "blue");
        addTagToGlobalList(tag1);
        addTagToGlobalList(tag2);
        tagItems.add(tag1.getText());
        tagItems.add(tag2.getText());
        autoCompleteTextView = binding.autocompleteTextview;
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.tag_list_item, tagItems);
        autoCompleteTextView.setAdapter(adapterItems);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if the fragment has received the tagInfo bundle
        if (getArguments() != null && getArguments().containsKey("tagInfo")) {
            tagInfo = getArguments().getStringArrayList("tagInfo");
            if (tagInfo != null && tagInfo.size() == 2) {
                tagName = tagInfo.get(0);
                tagColour = tagInfo.get(1);

                Tag tag = new Tag(tagName, tagColour);
                addTagToGlobalList(tag);

                tagItems.add(tagName);
                adapterItems.notifyDataSetChanged();
            }
        }

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                selectedTag = findTagByName(selectedItem);
            }
        });

        Button addTagButton = binding.addTagButton;
        addTagButton.setOnClickListener(v -> {

            if (items != null && selectedItem != null) {
                for (Item item : items) {
                    item.addTag(selectedTag);
                    selectedTag.addItem(item);
            }}

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

    }

    private void addTagToGlobalList(Tag tag) {
        if (!allTags.contains(tag)) {
            allTags.add(tag);
        }
    }
    private Tag findTagByName(String tagName) {
        for (Tag tag : allTags) {
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