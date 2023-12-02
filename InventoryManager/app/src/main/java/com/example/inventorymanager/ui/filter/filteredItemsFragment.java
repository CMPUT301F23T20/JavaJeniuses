package com.example.inventorymanager.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentFilteredItemsBinding;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Manages the screen that displays the list of items filtered by the requested conditions.
 * A brief summary of each item is displayed.
 * @author Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see chooseFilterFragment
 */
public class filteredItemsFragment extends Fragment {
    private FragmentFilteredItemsBinding binding;
    private ItemAdapter adapter;
    private ArrayList<Item> items;

    /**
     * Provides the user interface of the fragment.
     * Displays summary information about each of the filtered items.
     * @param inflater The object used to inflate views as required.
     * @param container The parent view of the fragment.
     * @param savedInstanceState The previous state of the fragment; not used in this fragment.
     * @return The root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilteredItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Bind the listview that displays our items
        ListView itemList = binding.itemList;

        // instantiate the shared view model which manages the database in case of any modifications to list i.e. deleting an item
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // unpack our list of filtered items
        items = getArguments().getParcelableArrayList("items");

        // bind our list to the listView
        adapter = new ItemAdapter(requireContext(), R.id.item_list, items);

        // Set the adapter for the ListView, allowing it to display the data
        itemList.setAdapter(adapter);
        // display the current total estimated value of items being displayed
        updateTotal();

        // display message if no items are found
        if (items.isEmpty()) {
            Toast.makeText(requireContext(), "No items found.", Toast.LENGTH_SHORT).show();
        }

        // add effect of clicking on a delete button (delete all highlighted items)
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( v-> {
            // delete all those items that are currently checked off
            // go backwards so we can delete in-place
            for (int i = items.size()-1; i >= 0; i--) {
                // check if this item is checked off through the adapter
                if (adapter.getIsChecked(items.get(i).getItemName())) {
                    // delete the present item if needed
                    itemViewModel.deleteItem(items.get(i).getItemName());
                    // remove the checked items from our displaying listview too
                    items.remove(i);
                }
            }
            // update listView adapter so that the deleted item is gone and price reflects this
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        // view item details when item clicked
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

        // sort items from filtered items page
        Button sortButton = root.findViewById(R.id.sort_button);
        sortButton.setOnClickListener(v -> {
            // send bundle with the list of items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", items);

            // navigate to the sort options fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.sortOptionsFragment, bundle);
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
