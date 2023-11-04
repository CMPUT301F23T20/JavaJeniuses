package com.example.inventorymanager.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ItemAdapter adapter;
    private ArrayList<Item> items;
//    private FirebaseFirestore db;
//    private CollectionReference itemsDB;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Bind the listview
        ListView itemList = binding.itemList;

//        db = FirebaseFirestore.getInstance();
//        itemsDB = db.collection("items");

        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
//        itemViewModel.fetchItems();

        // Add the "Car" item to the ViewModel if it's empty (This is just the initial item that
        // will be on the listview when app is booted, this is also a test to ensure the listview is
        // not getting overwritten when an item is added
        if (itemViewModel.getItemsLiveData().getValue() == null) {
            Item item = new Item("Sample Item", "0000-00-00", "Delete this once you've added new items.", "", "", 0.0, 0.0, "");
            itemViewModel.addItem(item);
//            itemsDB.document(item.getItemName()).set(item.getDocument());
        }
        // Create a new ArrayList to store the data that will be displayed in the ListView
        items = itemViewModel.getItemsLiveData().getValue();
        // Create an adapter to bind the data from the ArrayList to the ListView
        adapter = new ItemAdapter(requireContext(), items);

        // Create an instance of the shared ViewModel that manages the list of items
//        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Set the adapter for the ListView, allowing it to display the data
        itemList.setAdapter(adapter);


//        // Observe changes in the LiveData provided by the shared ViewModel (itemViewModel)
//        itemViewModel.getItemsLiveData().observe(getViewLifecycleOwner(), items -> {
//            // Clear the current data in the adapter to accurately represent the current state
//            adapter.clear();
//
//            // Add all the new items from the observed LiveData to the adapter
//            adapter.addAll(items);
//
//            // Notify the adapter that the data set has changed, triggering a UI update
//            adapter.notifyDataSetChanged();
//        });

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                Bundle bundle = new Bundle();
                bundle.putString("key", items.get(i).getItemName());
                navController.navigate(R.id.navigation_viewItem, bundle);
            }
        });


        // final TextView textView = binding.textHome;
        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
