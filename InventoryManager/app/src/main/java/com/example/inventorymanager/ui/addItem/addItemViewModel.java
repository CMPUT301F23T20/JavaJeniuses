package com.example.inventorymanager.ui.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventorymanager.ui.home.HomeFragment;

/**
 * A view model to manage the data for the addItemFragment.
 * Not used very much for the initial functionality of the project.
 * @author Kareem Assaf, Tomasz Ayobahan, Tyler Hoekstra, Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see addItemFragment
 */
public class addItemViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Generates the view model with default text.
     */
    public addItemViewModel() {
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