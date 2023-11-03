package com.example.inventorymanager.ui.editItem;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.databinding.FragmentEditItemBinding;
import com.example.inventorymanager.ui.addItem.addItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditItemFragment extends Fragment {

    private FragmentEditItemBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create an instance of the ViewModel for adding items
        EditItemViewModel editItemViewModel = new ViewModelProvider(this).get(EditItemViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentEditItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Bind UI elements to variables
        ScrollView editItemScrollView = binding.editItemScrollView;
        EditText itemNameInput = binding.itemNameInput;
        EditText purchaseDateInput = binding.purchaseDateInput;
        EditText descriptionInput = binding.descriptionInput;
        EditText makeInput = binding.makeInput;
        EditText modelInput = binding.modelInput;
        EditText serialNumberInput = binding.serialNumberInput;
        EditText estimatedValueInput = binding.estimatedValueInput;
        EditText commentInput = binding.commentInput;
        Button cancelButton = binding.cancelButton;
        Button saveButton = binding.saveButton;

        itemNameInput.setText("Assume this is the value already entered");
        purchaseDateInput.setText("Assume this is the value already entered");
        descriptionInput.setText("Assume this is the value already entered");
        makeInput.setText("Assume this is the value already entered");
        modelInput.setText("Assume this is the value already entered");
        serialNumberInput.setText("Assume this is the value already entered");
        estimatedValueInput.setText("Assume this is the value already entered");
        commentInput.setText("Assume this is the value already entered");

        cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        saveButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}