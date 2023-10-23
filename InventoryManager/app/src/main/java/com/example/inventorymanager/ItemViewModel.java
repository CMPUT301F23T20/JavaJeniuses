package com.example.inventorymanager;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ItemViewModel extends ViewModel {

        private MutableLiveData<ArrayList<Item>> itemsLiveData = new MutableLiveData<>();

        public LiveData<ArrayList<Item>> getItemsLiveData() {
            return itemsLiveData;
        }

        public void addItem(Item item) {
            ArrayList<Item> items = itemsLiveData.getValue();
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(item);
            itemsLiveData.setValue(items);
        }
}


