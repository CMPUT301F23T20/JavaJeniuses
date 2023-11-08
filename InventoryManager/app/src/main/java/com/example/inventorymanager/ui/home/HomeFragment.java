package com.example.inventorymanager.ui.home;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.inventorymanager.databinding.FragmentHomeBinding;

import com.example.inventorymanager.ui.sort.ItemSortDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private ArrayList<Item> items;
    private ItemSortDialog itemSortDialog;

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
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Bind the listview
        ListView itemList = binding.itemList;

        // instantiate the shared view model which manages the database
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Create a new ArrayList to store the data that will be displayed in the ListView
        items = itemViewModel.getItemsLiveData().getValue();
        // Create an adapter to bind the data from the ArrayList to the ListView
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
            adapter.notifyDataSetChanged();
            updateTotal();
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
        itemSortDialog = new ItemSortDialog();
        sortButton.setOnClickListener( v-> {
            // show sort dialog when sort icon clicked
            itemSortDialog.showSortDialog(requireContext(), items, adapter);
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
