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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Bind the listview
        ListView itemList = binding.itemList;

        // instantiate the shared view model which manages the database
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // add default item -- need to solve startup problem so we can remove this
        if (itemViewModel.getItemsLiveData().getValue() == null) {
            Item item = new Item("Sample Item", "0000-00-00", "Delete this once you've added new items.", "", "", 0.0, 0.0, "");
            itemViewModel.addItem(item);
        }

        // Create a new ArrayList to store the data that will be displayed in the ListView
        items = itemViewModel.getItemsLiveData().getValue();
        // Create an adapter to bind the data from the ArrayList to the ListView
        adapter = new ItemAdapter(requireContext(), items);

        // Set the adapter for the ListView, allowing it to display the data
        itemList.setAdapter(adapter);

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
