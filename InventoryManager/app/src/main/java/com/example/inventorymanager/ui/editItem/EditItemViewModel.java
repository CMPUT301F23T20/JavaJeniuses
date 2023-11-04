package com.example.inventorymanager.ui.editItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditItemViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public EditItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is edit item fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}