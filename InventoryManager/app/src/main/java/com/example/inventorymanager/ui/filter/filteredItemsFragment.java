package com.example.inventorymanager.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

/**
 * This class manages the page(fragment_filtered_items) that displays the list of items with the selected filter queries
 */
public class filteredItemsFragment extends Fragment {

    private FragmentFilteredItemsBinding binding;
    private ItemAdapter adapter;
    private ArrayList<Item> items;

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

        // add effect of clicking on a delete button (delete all highlighted items)
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( v-> {
            // delete all those items that are currently checked off
            // go backwards so we can delete in-place
            for (int i = items.size()-1; i >= 0; i--) {
                if (((CheckBox) itemList.getChildAt(i).findViewById(R.id.checkBox)).isChecked()) {
                    itemViewModel.deleteItem(items.get(i).getItemName());
                    // unselect each box that was previously checked
                    ((CheckBox) itemList.getChildAt(i).findViewById(R.id.checkBox)).setChecked(false);
                }
            }
            // update list so that the deleted item is gone and price reflects this
            // TODO:  BUG:: doesn't update list
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

        return root;
    }

    // updates the total estimated value being displayed on the screen
    public void updateTotal() {
        // calculate total estimated value
        double total = 0;
        for (Item item : items){
            total += Double.parseDouble(item.getEstimatedValue().substring(1));
        }

        // update the text view with the total estimated value formatted as money
        TextView totalTextView = binding.getRoot().findViewById(R.id.total_value);
        totalTextView.setText(String.format("$%.2f", total));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}