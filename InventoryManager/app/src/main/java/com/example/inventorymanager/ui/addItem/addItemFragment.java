package com.example.inventorymanager.ui.addItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.Item;

import java.util.ArrayList;


public class addItemFragment extends Fragment {

    private FragmentAddItemBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel addItemViewModel =
                new ViewModelProvider(this).get(addItemViewModel.class);

        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        EditText itemNameEditText = binding.itemNameEditText;
        EditText descriptionEditText = binding.descriptionEditText;
        EditText purchaseDateEditText = binding.purchaseDateEditText;
        EditText estimateValueEditText = binding.estimateValueEditText;
        Button addItemButton = binding.addItemButton;

        addItemButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String purchaseDate = purchaseDateEditText.getText().toString();
            String estimateValue = estimateValueEditText.getText().toString();

            if (!itemName.isEmpty() && !description.isEmpty() && !purchaseDate.isEmpty() && !estimateValue.isEmpty()) {
                Item newItem = new Item(itemName, purchaseDate, description,"", "", 0.0, Double.parseDouble(estimateValue), "");
                itemViewModel.addItem(newItem);

                // Clear the EditText fields
                itemNameEditText.setText("");
                descriptionEditText.setText("");
                purchaseDateEditText.setText("");
                estimateValueEditText.setText("");
            }
        });



        final TextView textView = binding.textAddItem;
        addItemViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}