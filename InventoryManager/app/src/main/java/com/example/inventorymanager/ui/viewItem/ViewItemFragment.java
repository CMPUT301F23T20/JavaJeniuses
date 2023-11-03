package com.example.inventorymanager.ui.viewItem;

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
import android.widget.TextView;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.databinding.FragmentViewItemBinding;
import com.example.inventorymanager.ui.addItem.addItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewItemFragment extends Fragment {

    private FragmentViewItemBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create an instance of the ViewModel for adding items
        ViewItemViewModel viewItemViewModel = new ViewModelProvider(this).get(ViewItemViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentViewItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create an instance of the shared ViewModel that manages the list of items
        ItemViewModel itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

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
        Button homeButton = binding.homeButton;
        Button editButton = binding.editButton;

        itemNameValue.setText("Car");
        purchaseDateValue.setText("2021-01-01");
        descriptionValue.setText("This is my car");
        makeValue.setText("Lamborghini");
        modelValue.setText("blah blah blah");
        serialNumberValue.setText("341343254214237");
        estimatedValueValue.setText("500,000.00");
        commentValue.setText("I like my car");

        homeButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        editButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_editItem);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    private ViewItemViewModel mViewModel;
//
//    public static ViewItemFragment newInstance() {
//        return new ViewItemFragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_view_item, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ViewItemViewModel.class);
//        // TODO: Use the ViewModel
//    }

}