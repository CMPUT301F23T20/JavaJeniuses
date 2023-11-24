package com.example.inventorymanager.ui.addTag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentAddTagBinding;
import com.example.inventorymanager.databinding.FragmentCreateTagBinding;

import java.util.ArrayList;


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

        tagColour = "red";

        redButton.setOnClickListener(v -> { tagColour = "red"; });
        blueButton.setOnClickListener(v -> { tagColour = "blue"; });
        greenButton.setOnClickListener(v -> { tagColour = "green"; });
        yellowButton.setOnClickListener(v -> { tagColour = "yellow"; });

        saveButton.setOnClickListener( v -> {
            String tagName = tagNameInput.getText().toString();
            ArrayList<String> tagInfo = new ArrayList<String>();
            tagInfo.add(tagName);
            tagInfo.add(tagColour);
            Log.d("myTag", "tag name" + tagInfo.get(0));
            Log.d("myTag", "tag colour" + tagInfo.get(1));

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("tagInfo", tagInfo);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment, bundle);
//            navController.getPreviousBackStackEntry().getSavedStateHandle().set("tagInfo", tagInfo);
//            navController.popBackStack();
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
