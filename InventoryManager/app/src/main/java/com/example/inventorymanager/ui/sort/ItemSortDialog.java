package com.example.inventorymanager.ui.sort;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.ItemAdapter;
import com.example.inventorymanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ItemSortDialog {
    private ItemSort itemSort;
    private String[] sortOptions;
    private boolean ascending;
    private boolean[] selectedOption;

    public void showSortDialog(Context context, ArrayList<Item> items, ItemAdapter adapter) {

        itemSort = new ItemSort();

        // array that holds user options for sorting the items
        sortOptions = new String[]{"Date", "Description", "Make", "Estimated Value"};
        selectedOption = new boolean[sortOptions.length];

        // initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sort Items By: ");
        builder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // inflate a custom layout for the dialog
        View customLayout = LayoutInflater.from(context).inflate(R.layout.custom_sort,null);
        builder.setView(customLayout);

        // find the arrow-up and arrow-down buttons in the custom layout
        ImageButton arrowUpButton = customLayout.findViewById(R.id.ascending_button);
        ImageButton arrowDownButton = customLayout.findViewById(R.id.descending_button);

        // set up listeners to handle button click
        arrowUpButton.setOnClickListener(v-> { ascending = true; });
        Log.d("MyTag", "ascending is " + ascending);
        arrowDownButton.setOnClickListener(v-> { ascending = false; });
        Log.d("MyTag", "ascending is " + ascending);

        // set dialog w options the user can choose from
        builder.setMultiChoiceItems(sortOptions, selectedOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    itemSort.sortItems(items, sortOptions[i], ascending);
                    Log.d("MyTag", "Item sorted");
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // handle ok button click and sort the list
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MyTag", "OK clicked");
                adapter.notifyDataSetChanged();
                Log.d("MyTag", "adapter notified");
                Log.d("MyTag", "items list " + items);
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
