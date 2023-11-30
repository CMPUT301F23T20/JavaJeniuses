package com.example.inventorymanager.ui.addTag;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.TagAdapter;
import com.example.inventorymanager.databinding.FragmentAddTagBinding;
import com.example.inventorymanager.databinding.FragmentSortOptionsBinding;
import com.example.inventorymanager.ui.filter.filteredItemsFragment;
import com.example.inventorymanager.ui.sort.sortedItemsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    private ArrayList<String> tagItems = new ArrayList<>();
    private String selectedItem;
    private FragmentAddTagBinding binding;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterTags;
    private static MutableLiveData<ArrayList<Tag>> tagsLiveData = new MutableLiveData<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Tag> allTags = new ArrayList<>();
    private Tag selectedTag;
    private Observer<ArrayList<Tag>> dataObserver;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentAddTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//
        // unpack all items sent to this fragment
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("items");
        }

        Button createTagButton = binding.createTagButton;

        createTagButton.setOnClickListener( v -> {

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.createTagFragment);
        });

        autoCompleteTextView = binding.autocompleteTextview;

        // create a listener so that the items being displayed automatically update based on database changes
        dataObserver = new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> updatedTags) {
                adapterTags = new TagAdapter(root.getContext(), R.layout.tag_list_item, updatedTags);
                autoCompleteTextView.setAdapter(adapterTags);
            }
        };

        // create default empty list on first time creating
        ArrayList<Tag> emptyTags = new ArrayList<>();
        tagsLiveData.setValue(emptyTags);

        getTagsLiveData().observe(getViewLifecycleOwner(), dataObserver);

        getTags();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getTags();

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
            }}

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

    }

    /**
     * Retrieves the data from the database that is currently being stored locally.
     * @return A list of the current tags being tracked.
     */
    public LiveData<ArrayList<Tag>> getTagsLiveData() {
        return tagsLiveData;
    }

    private void addTagToGlobalList(Tag tag) {
        if (!allTags.contains(tag)) {
            allTags.add(tag);
        }
    }
    private Tag findTagByName(String tagName) {
//        Log.d("DEBUG", "HERE");
        ArrayList<Tag> myTags= getTagsLiveData().getValue();
        for (Tag tag : myTags) {
//            Log.d("DEBUG", tag.getText());
            if (tag.getText().equals(tagName)) {
                return tag;
            }
        }
        return null; // Return null if tag not found
    }

    public void fetchTags() {
        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // set the tags database accessed to be for this one user
        CollectionReference tagsDB = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("tags");

        // query database to get all items indiscriminately
        tagsDB.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // fetch the results of the blank query
                            QuerySnapshot rawData = task.getResult();
                            List<DocumentSnapshot> cleanedData = rawData.getDocuments();
                            // iterate through fetched documents and make an item for each
                            ArrayList<Tag> tags = new ArrayList<>();
                            for (int i = 0; i < cleanedData.size(); i++) {
                                DocumentSnapshot rawTag = cleanedData.get(i);
                                // translate database format to the Item class
                                Tag cleanedTag = new Tag(
                                        rawTag.getString("name"),
                                        rawTag.getString("colour")
                                        );
                                tags.add(cleanedTag);
                            }
                            // update the items being shown to the what was fetched
                            tagsLiveData.setValue(tags);
                        }
                    }
                });
    }

    public void getTags() {
        fetchTags();

        ArrayList<Tag> tags = getTagsLiveData().getValue();
        // check which item corresponds to the given key and return it
        for (int i = 0; i < tags.size(); i++) {
            addTagToGlobalList(tags.get(i));
            tagItems.add(tags.get(i).getText());
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