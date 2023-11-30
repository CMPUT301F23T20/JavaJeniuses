package com.example.inventorymanager.ui.home;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.databinding.FragmentHomeBinding;

import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Shows the list of items being tracked by the application.
 * A brief summary of each item is displayed.
 * Users may choose to view the details of an item, delete items, filter items, or sort items.
 * @author Kareem Assaf, Tyler Hoekstra, Isaac Joffe, David Onchuru
 * @see Item
 * @see ItemViewModel
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ItemAdapter adapter;
    // No accessors and modifier methods. If you want to get items, instantiate itemViewModel and pull from database (same results)
    private ArrayList<Item> items, selectedItems;

    private Observer<ArrayList<Item>> dataObserver;

    /**
     * Provides the user interface of the fragment.
     * Queries the database to obtain detailed information about each item being tracked.
     * Displays summary information about each of these items.
     * Allows the user to view detailed information about each item being tracked and to delete multiple items at a time.
     * @param inflater The object used to inflate views as required.
     * @param container The parent view of the fragment.
     * @param savedInstanceState The previous state of the fragment; not used in this fragment.
     * @return The root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // basic setup functionality to set up view
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Bind the listview
        ListView itemList = binding.itemList;

        // instantiate the shared view model which manages the database
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // create a listener so that the items being displayed automatically update based on database changes
        dataObserver = new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> updatedItems) {
                // Create a new ArrayList to store the data that will be displayed in the ListView
                items = updatedItems;
                // Create an adapter to bind the data from the ArrayList to the ListView
                adapter = new ItemAdapter(requireContext(), R.id.item_list, items);
                // Set the adapter for the ListView, allowing it to display the data
                itemList.setAdapter(adapter);
                // display the current total estimated value of items being displayed
                updateTotal();
            }
        };

        // call this listener so that the items load in the first time
        itemViewModel.getItemsLiveData().observe(getViewLifecycleOwner(), dataObserver);

        // add effect of clicking on a delete button (delete all highlighted items)
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( v-> {
            // multi-delete causes issues for listeners, so temporarily disable
            itemViewModel.getItemsLiveData().removeObservers(getViewLifecycleOwner());
            // enter critical section

            // delete all those items that are currently checked off
            // go backwards so we can delete in-place
            for (int i = items.size()-1; i >= 0; i--) {
                // check if this item is checked off through the adapter
                if (adapter.getIsChecked(items.get(i).getItemName())) {
                    // delete the present item if needed
                    itemViewModel.deleteItem(items.get(i).getItemName());
                }
            }

            // exit critical section
            // update data being shown to reflect deletions
            itemViewModel.getItemsLiveData().observe(getViewLifecycleOwner(), dataObserver);
        });

        // add effect of clicking on an existing item (view item details)
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // send bundle with the item name, which is the database key
                Bundle bundle = new Bundle();
                bundle.putString("key", items.get(i).getItemName());

                // navigate to the view item screen (item details), sending data
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_viewItem, bundle);
            }
        });

        // add effect of clicking on a sort icon
        Button sortButton = root.findViewById(R.id.sort_button);
        sortButton.setOnClickListener( v-> {
            // send bundle with the list of items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", items);

            // navigate to the choose filter fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.sortOptionsFragment, bundle);
        });


        // add effect of clicking on a filter icon
        Button filterButton = root.findViewById(R.id.filter_button);
        // show filter dialog when filter icon clicked
        filterButton.setOnClickListener( v-> {
            // send bundle with the list of items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", items);

            // navigate to the choose filter fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.chooseFilterFragment, bundle);
        });

        // add effect of clicking on add tag button
        Button addTagButton = root.findViewById(R.id.tag_button);
        // show add button fragment when button clicked
        addTagButton.setOnClickListener( v-> {
            selectedItems = new ArrayList<Item>();

            for (int i = 0; i < items.size(); i++) {
                View childView = itemList.getChildAt(i);
                if (childView != null) {
                    CheckBox checkBox = childView.findViewById(R.id.checkBox);
                    if (checkBox != null && checkBox.isChecked()) {
                        selectedItems.add(items.get(i));
                        ((CheckBox) itemList.getChildAt(i).findViewById(R.id.checkBox)).setChecked(false);
                    }
                }
            }

            // send bundle with the list of items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", selectedItems);

            // navigate to the add tag fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment, bundle);

        });

        return root;
    }

    /**
     * Updates the total estimated monetary value being displayed on the screen.
     * Computes the proper total value based on addition of each displayed item's estimated monetary value.
     */
    public void updateTotal() {
        // calculate total estimated value
        double total = 0;
        for (Item item : items){
            // must eliminate the $ and , characters to read as a number
            total += Double.parseDouble(item.getEstimatedValue().replaceAll("[$,]", ""));
        }

        // update the text view with the total estimated value formatted as money
        TextView totalTextView = binding.getRoot().findViewById(R.id.total_value);
        totalTextView.setText(NumberFormat.getCurrencyInstance().format(total));
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
