package com.example.inventorymanager.ui.viewItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewItemViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ViewItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is view item fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}