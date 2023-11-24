package com.example.inventorymanager.ui.addTag;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventorymanager.ui.editItem.EditItemFragment;

/**
 * A view model to manage the data for the createTagFragment.
 * Not used very much for the initial functionality of the project.
 * @author Kareem Assaf, Tomasz Ayobahan, Tyler Hoekstra, Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see EditItemFragment
 */
public class createTagViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    /**
     * Creates the view model with default text.
     */
    public createTagViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the create tag fragment.");
    }

    /**
     * Retrieves the text being stored within the view model.
     * @return The text being stored within the view model.
     */
    public LiveData<String> getText() {
        return mText;
    }
}