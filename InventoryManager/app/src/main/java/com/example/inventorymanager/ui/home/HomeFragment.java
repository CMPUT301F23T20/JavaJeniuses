package com.example.inventorymanager.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemFilter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.MainActivity;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ItemAdapter adapter;
    private ArrayList<Item> items;
    private ItemFilter itemFilter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            // update list so that the deleted item is gone
            adapter.notifyDataSetChanged();
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

        // add effect of clicking on a filter icon
        Button filterButton = root.findViewById(R.id.filter_button);
        itemFilter = new ItemFilter();

        // show filter dialog when filter icon clicked
        filterButton.setOnClickListener( v-> {
            itemFilter.showFilterDialog(requireContext());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
