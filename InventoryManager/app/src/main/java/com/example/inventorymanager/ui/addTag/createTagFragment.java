package com.example.inventorymanager.ui.addTag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventorymanager.ItemViewModel;
import com.example.inventorymanager.R;
import com.example.inventorymanager.TagViewModel;
import com.example.inventorymanager.databinding.FragmentAddTagBinding;
import com.example.inventorymanager.databinding.FragmentCreateTagBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class createTagFragment extends Fragment {

    private FragmentCreateTagBinding binding;
    private String tagColour;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCreateTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind UI elements to variables
        EditText tagNameInput = binding.tagEditText;
        Button redButton = binding.redButton;
        Button blueButton = binding.blueButton;
        Button greenButton = binding.greenButton;
        Button yellowButton = binding.yellowButton;
        Button cancelButton = binding.cancelButton;
        Button saveButton = binding.saveButton;

        // default tag colour is red
        tagColour = "red";

        // change colour based on user's choice
        redButton.setOnClickListener(v -> { tagColour = "red"; });
        blueButton.setOnClickListener(v -> { tagColour = "blue"; });
        greenButton.setOnClickListener(v -> { tagColour = "green"; });
        yellowButton.setOnClickListener(v -> { tagColour = "yellow"; });

        addTagViewModel publicTagViewModel = new ViewModelProvider(requireActivity()).get(addTagViewModel.class);

        saveButton.setOnClickListener( v -> {
            String tagName = tagNameInput.getText().toString();

            publicTagViewModel.addTag(tagName, tagColour);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment);
        });

        cancelButton.setOnClickListener( v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment);
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
