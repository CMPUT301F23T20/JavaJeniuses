package com.example.inventorymanager.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class cameraViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Generates the view model with default text.
     */
    public cameraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add item fragment");
    }

    /**
     * Retrieves the text being stored within the view model.
     * @return The text being stored within the view model.
     */
    public LiveData<String> getText() {
        return mText;
    }
}
