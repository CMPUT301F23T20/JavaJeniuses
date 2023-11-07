package com.example.inventorymanager.ui.viewItem;

import static java.lang.Thread.sleep;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.MainActivity;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.databinding.FragmentViewItemBinding;
import com.example.inventorymanager.ui.addItem.addItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Shows the details of a single item.
 * Each field of the item is labelled and displayed.
 * Users may choose to edit the details of this item or to delete this item.
 * @author Isaac Joffe
 * @see com.example.inventorymanager.ui.home.HomeFragment
 * @see com.example.inventorymanager.ui.editItem.EditItemFragment
 */
public class ViewItemFragment extends Fragment {

    private FragmentViewItemBinding binding;

    /**
     * Provides the user interface of the fragment.
     * Receives an item label from the calling fragment and queries the database to obtain detailed information about the item.
     * Displays this item information.
     * Provides buttons for the user to choose to edit the details of this item or to delete this item.
     * @param inflater The object used to inflate views as required.
     * @param container The parent view of the fragment.
     * @param savedInstanceState The previous state of the fragment; not used in this fragment.
     * @return The root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create an instance of the ViewModel for adding items
        ViewItemViewModel viewItemViewModel = new ViewModelProvider(this).get(ViewItemViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentViewItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // get the argument passed representing the item of interest
        String key = getArguments().getString("key");
        // fetch the full item from the database
        Item item = itemViewModel.getItem(key);

        // Bind UI elements to variables
        ScrollView viewItemScrollView = binding.ViewItemScrollView;
        TextView itemNameValue = binding.itemNameValue;
        TextView purchaseDateValue = binding.purchaseDateValue;
        TextView descriptionValue = binding.descriptionValue;
        TextView makeValue = binding.makeValue;
        TextView modelValue = binding.modelValue;
        TextView serialNumberValue = binding.serialNumberValue;
        TextView estimatedValueValue = binding.estimatedValueValue;
        TextView commentValue = binding.commentValue;
        Button editButton = binding.editButton;
        Button deleteButton = binding.deleteButton;

        // set the text view to show the values that item already has
        itemNameValue.setText(item.getItemName());
        purchaseDateValue.setText(item.getPurchaseDate());
        descriptionValue.setText(item.getDescription());
        makeValue.setText(item.getMake());
        modelValue.setText(item.getModel());
        serialNumberValue.setText(item.getSerialNumber());
        estimatedValueValue.setText(item.getEstimatedValue());
        commentValue.setText(item.getComment());

        // add effect of the edit button when pressed (edit details)
        editButton.setOnClickListener(v -> {
            // send bundle with the item name, which is the database key
            Bundle bundle = new Bundle();
            bundle.putString("key", item.getItemName());

            // navigate to the edit item screen (edit details), sending data
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_editItem, bundle);
        });

        // add effect of the delete button when pressed (delete this item)
        deleteButton.setOnClickListener(v -> {
            // delete this item from the database
            itemViewModel.deleteItem(key);

            // navigate back to the app home screen (item list)
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        return root;
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