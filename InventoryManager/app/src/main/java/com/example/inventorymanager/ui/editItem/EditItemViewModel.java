package com.example.inventorymanager.ui.editItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * A view model to manage the data for the EditItemFragment.
 * Not used very much for the initial functionality of the project.
 */
public class EditItemViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    /**
     * Creates the view model with default text.
     */
    public EditItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the edit item fragment.");
    }

    /**
     * Retrieves the text being stored within the view model.
     * @return The text being stored within the view model.
     */
    public LiveData<String> getText() {
        return mText;
    }
}