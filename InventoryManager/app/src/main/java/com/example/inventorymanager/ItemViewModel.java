package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * The means by which the system interacts with the backend database.
 * This class interfaces with the API to access the Firestore database of items for any given user.
 * All database-related queries are completed via this class, which provides an API for basic CRUD operations.
 * Create operations are completed through addItem().
 * Read operations are completed through getItem() and fetchItems().
 * Update operations are completed through editItem().
 * Delete operations are completed through deleteItem().
 * This class utilizes static variables to share global access to the same database, which is
 * required to provide basic application functionality.
 * The database key is the name of the item.
 * @author Isaac Joffe
 * @see Item
 * @see com.example.inventorymanager.ui.home.HomeFragment
 */
public class ItemViewModel extends ViewModel {
    // static variables make it so one copy of this variable exists across the whole app, synchronization
    private static MutableLiveData<ArrayList<Item>> itemsLiveData = new MutableLiveData<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference itemsDB = db.collection("items");

    /**
     * Creates an ItemViewModel() object synced to the global application database.
     */
    public ItemViewModel() {
        // create default empty list on first time creating
        ArrayList<Item> items = new ArrayList<>();
        itemsLiveData.setValue(items);
        // get the current system state from the database
        fetchItems();
    }

    /**
     * Retrieves the data from the database that is currently being stored locally.
     * @return A list of the current items being tracked.
     */
    public LiveData<ArrayList<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    /**
     * Fetches the entire list of items being tracked inside the database.
     * Updates the local copy of the entire set of objects being tracked for speed and use across all of the application.
     */
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

    /**
     * Retrieves a single item matching a certain database key value.
     * It is assumed that an item with the given key exists in the database.
     * @param key The name of the object whose data is to be fetched.
     * @return The item in the database with the given key.
     */
    public Item getItem(String key) {
        // get the current items being tracked
        fetchItems();
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

    /**
     * Adds a single item to the database.
     * It is assumed that this item does not violate any database key constraints (as validated by ItemUtility()).
     * @param item The item to be added to the database.
     */
    public void addItem(Item item) {
        // get the current items being tracked
        fetchItems();
        ArrayList<Item> items = getItemsLiveData().getValue();
        // add the new item locally and to the database
        items.add(item);
        itemsDB.document(item.getItemName()).set(item.getDocument());
        // save the new state of items being tracked
        itemsLiveData.setValue(items);
        fetchItems();
    }

    /**
     * Updates a single existing item in the database.
     * The item is not updated in-place; the old item is deleted and the new version is added.
     * This prevents any issues associated with changing the name (key) of an item.
     * @param key The previous name of the item being updated.
     * @param item The new version of the item being updated.
     */
    public void editItem(String key, Item item) {
        // get the current items being tracked
        fetchItems();
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

    /**
     * Deletes a single existing item in the database.
     * It is assumed that this item exists in the database; no warning is provided if it does not.
     * @param key The name of the item to be deleted.
     */
    public void deleteItem(String key) {
        // get the current items being tracked
        fetchItems();
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

    /**
     * Checks whether a proposed item name is legal or not.
     * If an existing item's name is not changed, it is always legal.
     * If an existing item's name is changed or a new item's name is proposed, it is only legal if and only if that name is not taken by any other item.
     * @param oldName The previous name of the item. For new items, this parameter should be set to "".
     * @param newName The new proposed name of the item.
     * @return TRUE if the name change is illegal (i.e., the name is already taken); FALSE if the name change is legal.
     */
    public boolean isIllegalNameChange(String oldName, String newName) {
        // if editing an item and names match, that is ok
        if (oldName.equals(newName)) {
            return false;
        }
        // get the current items being tracked
        fetchItems();
        ArrayList<Item> items = getItemsLiveData().getValue();
        // check if the item name is already taken
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(newName)) {
                return true;
            }
        }
        // no match found, so the name must be new and therefore legal
        return false;
    }
}


