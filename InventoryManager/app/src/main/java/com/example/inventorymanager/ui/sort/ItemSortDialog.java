package com.example.inventorymanager.ui.sort;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ItemSortDialog {

    public void showSortDialog(Context context, ArrayList<Item> items) {

        // array that holds user options for sorting the items
        String[] sortOptions = {"Date", "Description", "Make", "Estimated Value"};
        boolean[] selectedOption = new boolean[sortOptions.length];

        // initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sort Items By: ");
        builder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // set dialog w options the user can choose from
        builder.setMultiChoiceItems(sortOptions, selectedOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                System.out.println("hello");}
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // handle ok button click and sort the list
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // skip
                System.out.println("Yay.");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // handle cancel button click and dismiss dialog
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}
