package com.example.inventorymanager.ui.viewItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * A view model to manage the data for the ViewItemFragment.
 * Not used very much for the initial functionality of the project.
 * @author Kareem Assaf, Tomasz Ayobahan, Tyler Hoekstra, Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see ViewItemFragment
 */
public class ViewItemViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    /**
     * Creates the view model with default text.
     */
    public ViewItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the view item fragment.");
    }

    /**
     * Retrieves the text being stored within the view model.
     * @return The text being stored within the view model.
     */
    public LiveData<String> getText() {
        return mText;
    }
}