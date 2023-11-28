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
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference publicTagsRef;

    private static final String TAG = "AddTag";

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

        saveButton.setOnClickListener( v -> {
            String tagName = tagNameInput.getText().toString();
            ArrayList<String> tagInfo = new ArrayList<String>();
            tagInfo.add(tagName);
            tagInfo.add(tagColour);

            addTag(tagName);

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("tagInfo", tagInfo);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment, bundle);
        });

        cancelButton.setOnClickListener( v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.addTagFragment);
        });

        return root;
    }

    private void addTag(String tagName) {
        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // set the tags database accessed to be for this one user
        publicTagsRef = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("tags");

        // add colour in a Hashmap to make into a field
        Map<String, Object> addTagColour = new HashMap<>();
        addTagColour.put("name", tagName);
        addTagColour.put("colour", tagColour);

        // add tag colour as a field to the database, updates the colour of a tag that already exists
        publicTagsRef.document(tagName)
                .set(addTagColour, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
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
