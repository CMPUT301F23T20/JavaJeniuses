package com.example.inventorymanager.ui.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class addItemViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public addItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add item fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}