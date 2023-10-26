package com.example.inventorymanager.ui.addItem;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.Item;

import java.util.ArrayList;


public class addItemFragment extends Fragment {

    private FragmentAddItemBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Create an instance of the ViewModel for adding items
        addItemViewModel addItemViewModel =
                new ViewModelProvider(this).get(addItemViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Bind UI elements to variables
        ScrollView addItemScrollView = binding.addItemScrollView;
        EditText itemNameInput = binding.itemNameInput;
        EditText purchaseDateInput = binding.purchaseDateInput;
        EditText descriptionInput = binding.descriptionInput;
        EditText makeInput = binding.makeInput;
        EditText modelInput = binding.modelInput;
        EditText serialNumberInput = binding.serialNumberInput;
        EditText estimatedValueInput = binding.estimatedValueInput;
        EditText commentInput = binding.commentInput;
        Button addItemButton = binding.addItemButton;

        // Set up behavior to scroll once the commentInput EditText is filled out to scroll and reveal button
        // Close keyboard as we've reached the end of input fields
        commentInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addItemScrollView.post(() -> addItemScrollView.scrollTo(0, addItemButton.getBottom()));
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });


        addItemButton.setOnClickListener(v -> {
            // Get data from input fields
            String itemName = itemNameInput.getText().toString();
            String purchaseDate = purchaseDateInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String make = makeInput.getText().toString();
            String model = modelInput.getText().toString();
            String serialNumber = serialNumberInput.getText().toString();
            String estimateValue = estimatedValueInput.getText().toString();
            String comment = commentInput.getText().toString();

            // Check if required fields are not empty (This is just a brief validation check, needs to be adjusted for future specifications

            if (!itemName.isEmpty() && !purchaseDate.isEmpty() && !description.isEmpty() && !make.isEmpty()
                    && !model.isEmpty() && !serialNumber.isEmpty() && !estimateValue.isEmpty() && !comment.isEmpty()) {

                // Create a new item using the filled out fields
                Item newItem = new Item(itemName, purchaseDate, description,model, make, Double.parseDouble(serialNumber), Double.parseDouble(estimateValue), comment);

                // Add the new item to the shared ViewModel
                itemViewModel.addItem(newItem);

                // Navigate back to the home fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_home);

                // Clear the EditText fields
                itemNameInput.setText("");
                purchaseDateInput.setText("");
                descriptionInput.setText("");
                makeInput.setText("");
                modelInput.setText("");
                serialNumberInput.setText("");
                estimatedValueInput.setText("");
                commentInput.setText("");


            }

        });




        // I'm not quite sure what this is for so I just commented it out
        //final TextView textView = binding.textAddItem;
        //addItemViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}