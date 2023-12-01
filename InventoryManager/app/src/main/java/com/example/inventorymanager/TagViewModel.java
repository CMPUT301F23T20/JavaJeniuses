//package com.example.inventorymanager;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.inventorymanager.ui.addTag.addTagFragment;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.SetOptions;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.sql.StatementEvent;
//
//public class TagViewModel extends ViewModel {
//    // static variables make it so one copy of this variable exists across the whole app, synchronization
//    private static MutableLiveData<ArrayList<Tag>> itemTagsLiveData = new MutableLiveData<>();
//    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private static CollectionReference itemTagsRef;
//    private String itemName;
//
//    public TagViewModel(String itemName) {
//        // get the user from firebase
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        // set the database accessed to be for this one user
//        itemTagsRef = db.collection("users")
//                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
//                .collection("items").document(itemName).collection("tags");
//        // create default empty list on first time creating
//        ArrayList<Tag> tags = new ArrayList<>();
//        itemTagsLiveData.setValue(tags);
//        // get the current system state from the database
//        fetchItemTags();
//    }
//
//    public LiveData<ArrayList<Tag>> getItemTagsLiveData() {
//        return itemTagsLiveData;
//    }
//
//    public void fetchItemTags() {
//        // query database to get all items indiscriminately
//        itemTagsRef.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            // fetch the results of the blank query
//                            QuerySnapshot rawData = task.getResult();
//                            List<DocumentSnapshot> cleanedData = rawData.getDocuments();
//                            // iterate through fetched documents and make an item for each
//                            ArrayList<Tag> tags = new ArrayList<>();
//                            for (int i = 0; i < cleanedData.size(); i++) {
//                                DocumentSnapshot rawTag = cleanedData.get(i);
//                                // translate database format to the Item class
//                                Tag cleanedTag = new Tag(
//                                        rawTag.getString("name"),
//                                        rawTag.getString("colour")
//                                );
//                                tags.add(cleanedTag);
//                            }
//                            // update the items being shown to the what was fetched
//                            itemTagsLiveData.setValue(tags);
//                        }
//                    }
//                });
//    }
//
//}

package com.example.inventorymanager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventorymanager.ui.addTag.addTagFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A view model to manage the data for the addTagFragment.
 * Not used very much for the initial functionality of the project.
 * @author Tomasz Ayobahan, Sumaiya Salsabil
 * @see addTagFragment
 */
public class TagViewModel extends ViewModel {
    private static MutableLiveData<ArrayList<Tag>> tagsLiveData = new MutableLiveData<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference publicTagsRef;

    /**
     * Creates the view model with default text.
     */
    public TagViewModel() {
        // get the user from firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // set the tags database accessed to be for this one user
        publicTagsRef = db.collection("users")
                .document(user.getEmail().substring(0, user.getEmail().indexOf('@')))
                .collection("tags");

        // create default empty list on first time creating
        ArrayList<Tag> emptyTags = new ArrayList<>();
        tagsLiveData.setValue(emptyTags);

        fetchTags();
    }

    /**
     * Retrieves the data from the database that is currently being stored locally.
     * @return A list of the current tags being tracked.
     */
    public LiveData<ArrayList<Tag>> getTagsLiveData() {
        return tagsLiveData;
    }

    /**
     *
     */
    public void fetchTags() {
        // query database to get all items indiscriminately
        publicTagsRef.get()
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
                            tagsLiveData.setValue(tags);
                        }
                    }
                });
    }

    public void addTag(Tag tag) {
        // add colour in a Hashmap to make into a field
        HashMap<String, String> newTag = new HashMap<>();
        newTag.put("name", tag.getText());
        newTag.put("colour", tag.getColour());

        // add tag colour as a field to the database, updates the colour of a tag that already exists
        publicTagsRef.document(tag.getText())
                .set(newTag, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }

    public void getTags() {
        fetchTags();

        ArrayList<Tag> tags = getTagsLiveData().getValue();
        // check which item corresponds to the given key and return it
        for (int i = 0; i < tags.size(); i++) {
        }
    }
}
