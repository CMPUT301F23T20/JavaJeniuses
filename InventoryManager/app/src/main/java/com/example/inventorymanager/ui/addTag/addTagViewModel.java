package com.example.inventorymanager.ui.addTag;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventorymanager.ui.editItem.EditItemFragment;

/**
 * A view model to manage the data for the addTagFragment.
 * Not used very much for the initial functionality of the project.
 * @author Kareem Assaf, Tomasz Ayobahan, Tyler Hoekstra, Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see addTagFragment
 */
public class addTagViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    /**
     * Creates the view model with default text.
     */
    public addTagViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the add tag fragment.");
    }

    /**
     * Retrieves the text being stored within the view model.
     * @return The text being stored within the view model.
     */
    public LiveData<String> getText() {
        return mText;
    }
}