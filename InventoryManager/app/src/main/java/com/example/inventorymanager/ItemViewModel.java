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
//    private Item currentItem;

    public ItemViewModel() {
        fetchItems();
//        itemsDB.get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot rawData = task.getResult();
//                        List<DocumentSnapshot> cleanedData = rawData.getDocuments();
//                        ArrayList<Item> items = new ArrayList<Item>();
//                        for (int i = 0; i < cleanedData.size(); i++) {
//                            DocumentSnapshot rawItem = cleanedData.get(i);
//                            Item cleanedItem = new Item(
//                                    (String) rawItem.get("name"),
//                                    (String) rawItem.get("date"),
//                                    (String) rawItem.get("description"),
//                                    (String) rawItem.get("model"),
//                                    (String) rawItem.get("make"),
//                                    Double.valueOf((String) rawItem.get("number")),
//                                    Double.valueOf((String) rawItem.get("value")),
//                                    (String) rawItem.get("comment"));
//                            items.add(cleanedItem);
//                        }
//                        itemsLiveData.setValue(items);
//                    }
//                }
//            });
//        QuerySnapshot rawData = itemsDB.get().getResult();
//        List<HashMap> cleanedData = rawData.toObjects(HashMap.class);
//        ArrayList<Item> items = new ArrayList<Item>();
//        for (int i = 0; i < cleanedData.size(); i++) {
//            HashMap<String, String> rawItem = cleanedData.get(i);
//            Item cleanedItem = new Item(rawItem.get("name"),
//                    rawItem.get("date"),
//                    rawItem.get("description"),
//                    rawItem.get("model"),
//                    rawItem.get("make"),
//                    Double.valueOf(rawItem.get("number")),
//                    Double.valueOf(rawItem.get("value")),
//                    rawItem.get("comment"));
//            items.add(cleanedItem);
//        }
//        itemsLiveData.setValue(items);
    }

    public LiveData<ArrayList<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public void fetchItems() {
        itemsDB.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot rawData = task.getResult();
                        List<DocumentSnapshot> cleanedData = rawData.getDocuments();
                        ArrayList<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < cleanedData.size(); i++) {
                            DocumentSnapshot rawItem = cleanedData.get(i);
                            Item cleanedItem = new Item(
                                    (String) rawItem.get("name"),
                                    (String) rawItem.get("date"),
                                    (String) rawItem.get("description"),
                                    (String) rawItem.get("model"),
                                    (String) rawItem.get("make"),
                                    Double.valueOf((String) rawItem.get("number")),
                                    Double.valueOf((String) rawItem.get("value")),
                                    (String) rawItem.get("comment"));
                            items.add(cleanedItem);
                        }
                        itemsLiveData.setValue(items);
                    }
                }
            });
    }

    public Item getItem(String key) {
        ArrayList<Item> items = getItemsLiveData().getValue();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                return items.get(i);
            }
        }
        return new Item("Error", "", "", "", "", 0.0, 0.0, "");
//        Item item = new Item("", "", "", "", "", 0.0, 0.0, "");
//        Log.d("test", key + "2");
//        Log.d("test", key + "3");
//        itemsDB.get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    Log.d("test", key + "3");
//                    if (task.isSuccessful()) {
//                        Log.d("test", key + "4");
//                        QuerySnapshot rawData = task.getResult();
//                        List<DocumentSnapshot> cleanedData = rawData.getDocuments();
//                        for (int i = 0; i < cleanedData.size(); i++) {
//                            DocumentSnapshot rawItem = cleanedData.get(i);
//                            if (((String) rawItem.get("name")).equals(key)) {
//                                currentItem = new Item(
//                                        (String) rawItem.get("name"),
//                                        (String) rawItem.get("date"),
//                                        (String) rawItem.get("description"),
//                                        (String) rawItem.get("model"),
//                                        (String) rawItem.get("make"),
//                                        Double.valueOf((String) rawItem.get("number")),
//                                        Double.valueOf((String) rawItem.get("value")),
//                                        (String) rawItem.get("comment"));
//                            }
//                        }
//                    }
//                }
//            });
//        itemsDB.whereEqualTo("name", key).get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                    Log.d("test", "here");
//                    if (task.isSuccessful()) {
//                        QuerySnapshot rawData = task.getResult();
//                        List<DocumentSnapshot> cleanedData = rawData.getDocuments();
//                        DocumentSnapshot rawItem = cleanedData.get(0);
//                        currentItem = new Item(
//                                (String) rawItem.get("name"),
//                                (String) rawItem.get("date"),
//                                (String) rawItem.get("description"),
//                                (String) rawItem.get("model"),
//                                (String) rawItem.get("make"),
//                                Double.valueOf((String) rawItem.get("number")),
//                                Double.valueOf((String) rawItem.get("value")),
//                                (String) rawItem.get("comment"));
//                    }
//                }
//            });
//        try {
//            Thread.sleep(3000);
//        } catch (Exception e) {
//            Log.d("test", "sleep error");
//        }
//        return currentItem;
    }

    public void addItem(Item item) {
        ArrayList<Item> items = itemsLiveData.getValue();
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        itemsDB.document(item.getItemName()).set(item.getDocument());
        itemsLiveData.setValue(items);
        itemsDB.document(item.getItemName()).set(item.getDocument());
//        fetchItems();
    }

    public void editItem(String key, Item item) {
        ArrayList<Item> items = getItemsLiveData().getValue();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                items.remove(i);
                itemsDB.document(key).delete();
                items.add(item);
                itemsDB.document(item.getItemName()).set(item.getDocument());
                itemsLiveData.setValue(items);
                fetchItems();
                return;
            }
        }
    }

    public void deleteItem(String key) {
        ArrayList<Item> items = getItemsLiveData().getValue();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equals(key)) {
                items.remove(i);
                itemsDB.document(key).delete();
                itemsLiveData.setValue(items);
                fetchItems();
                return;
            }
        }
    }
}


