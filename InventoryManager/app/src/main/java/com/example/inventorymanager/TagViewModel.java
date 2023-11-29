package com.example.inventorymanager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.sql.StatementEvent;

public class TagViewModel extends ViewModel {
    // static variables make it so one copy of this variable exists across the whole app, synchronization
    private static MutableLiveData<ArrayList<Tag>> itemTagsLiveData = new MutableLiveData<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference itemTagsRef;
    private String itemName;

    public TagViewModel(String itemName) {
        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // set the database accessed to be for this one user
        itemTagsRef = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("items").document(itemName).collection("tags");
        // create default empty list on first time creating
        ArrayList<Tag> tags = new ArrayList<>();
        itemTagsLiveData.setValue(tags);
        // get the current system state from the database
        fetchItemTags();
    }

    public LiveData<ArrayList<Tag>> getItemTagsLiveData() {
        return itemTagsLiveData;
    }

    public void fetchItemTags() {
        // query database to get all items indiscriminately
        itemTagsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // fetch the results of the blank query
                            QuerySnapshot rawData = task.getResult();
                            List<DocumentSnapshot> cleanedData = rawData.getDocuments();
                            // iterate through fetched documents and make an item for each
                            ArrayList<Tag> tags = new ArrayList<>();
                            for (int i = 0; i < cleanedData.size(); i++) {
                                DocumentSnapshot rawTag = cleanedData.get(i);
                                // translate database format to the Item class
                                Tag cleanedTag = new Tag(
                                        rawTag.getString("name"),
                                        rawTag.getString("colour")
                                );
                                tags.add(cleanedTag);
                            }
                            // update the items being shown to the what was fetched
                            itemTagsLiveData.setValue(tags);
                        }
                    }
                });
    }

}
