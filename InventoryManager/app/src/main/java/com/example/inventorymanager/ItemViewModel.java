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

    public void addItem(Item item) {
        ArrayList<Item> items = itemsLiveData.getValue();
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        itemsDB.document(item.getItemName()).set(item.getDocument());
        itemsLiveData.setValue(items);
    }

    public void editItem() {

    }

    public void deleteItem(int i) {
        ArrayList<Item> items = itemsLiveData.getValue();
        if (items == null) {
            items = new ArrayList<>();
        }
        itemsDB.document(items.get(i).getItemName()).delete();
        items.remove(i);
        itemsLiveData.setValue(items);
    }
}


