package com.example.inventorymanager;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemViewModel extends ViewModel {

    private static MutableLiveData<ArrayList<Item>> itemsLiveData = new MutableLiveData<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference itemsDB = db.collection("items");

    public ItemViewModel() {
        // create default empty list on first time creating
        ArrayList<Item> items = new ArrayList<>();
        itemsLiveData.setValue(items);
        // get the current system state from the database
        fetchItems();
    }

    public LiveData<ArrayList<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    // fetch the whole list of items
    public void fetchItems() {
        // query database to get all items indiscriminately
        itemsDB.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        // fetch the results of the blank query
                        QuerySnapshot rawData = task.getResult();
                        List<DocumentSnapshot> cleanedData = rawData.getDocuments();
                        // iterate through fetched documents and make an item for each
                        ArrayList<Item> items = new ArrayList<>();
                        for (int i = 0; i < cleanedData.size(); i++) {
                            DocumentSnapshot rawItem = cleanedData.get(i);
                            // translate database format to the Item class
                            Item cleanedItem = new Item(
                                    (String) rawItem.get("name"),
                                    (String) rawItem.get("date"),
                                    (String) rawItem.get("description"),
                                    (String) rawItem.get("model"),
                                    (String) rawItem.get("make"),
                                    (String) rawItem.get("number"),
                                    (String) rawItem.get("value"),
                                    (String) rawItem.get("comment"));
                            items.add(cleanedItem);
                        }
                        // update the items being shown to the what was fetched
                        itemsLiveData.setValue(items);
                    }
                }
            });
    }

    // get a single item matching a certain key
    public Item getItem(String key) {
        // get the current items being tracked
        ArrayList<Item> items = getItemsLiveData().getValue();
        // check which item corresponds to the given key and return it
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                return items.get(i);
            }
        }

        // should never ever be reached
        return new Item("Error", "", "", "", "", "", "", "");
    }

    // add an item to the database and what is being shown
    public void addItem(Item item) {
        // get the current items being tracked
        ArrayList<Item> items = getItemsLiveData().getValue();
        // add the new item locally and to the database
        items.add(item);
        itemsDB.document(item.getItemName()).set(item.getDocument());
        // save the new state of items being tracked
        itemsLiveData.setValue(items);
        fetchItems();
    }

    // edit an existing item in the database and what is shown
    public void editItem(String key, Item item) {
        // get the current items being tracked
        ArrayList<Item> items = getItemsLiveData().getValue();
        // find and update the item corresponding to the given search key
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                // remove item locally and from the database
                items.remove(i);
                itemsDB.document(key).delete();
                // add the new item back locally and to the database
                items.add(item);
                itemsDB.document(item.getItemName()).set(item.getDocument());
                // save the new state of items being tracked
                itemsLiveData.setValue(items);
                fetchItems();
                return;
            }
        }
    }

    // delete an exitsing item in the database and from what is shown
    public void deleteItem(String key) {
        // get the current items being tracked
        ArrayList<Item> items = getItemsLiveData().getValue();
        // find and delete the item corresponding to the given search key
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                // remove item locally and from the database
                items.remove(i);
                itemsDB.document(key).delete();
                // save the new state of items being tracked
                itemsLiveData.setValue(items);
                fetchItems();
                return;
            }
        }
    }
}


