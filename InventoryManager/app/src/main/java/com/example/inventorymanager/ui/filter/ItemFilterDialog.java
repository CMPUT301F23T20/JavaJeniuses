package com.example.inventorymanager.ui.filter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.R;
import com.example.inventorymanager.Item;
import com.example.inventorymanager.ui.home.HomeFragment;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class is now DEFUNCT - will be deleted when doing final refactoring because there could be useful functions in here
 */

public class ItemFilterDialog extends Fragment {

    private String startDate;
    private String endDate;
    private String keyword;
    private String make;
    private ArrayList<Item> existingItems;

    public ArrayList<Item> itemsByFilter = new ArrayList<>();

    public void showFilterDialog(Context context) {

        // array that holds user options for filtering the items
        String[] filterOptions = {"Date Range", "Description Keyword", "Make"};
        boolean[] selectedOption = new boolean[filterOptions.length];

        // initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Filter Items By: ");
        builder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // set dialog w options the user can choose from
        builder.setMultiChoiceItems(filterOptions, selectedOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // if user chooses to filter by date range, date input field pops up
                if (filterOptions[i].equals("Date Range")) { showCustomDateRangeFields(context); }

                // if user chooses to filter by keyword or make, user input field pops up
                else { collectUserInput(context, filterOptions[i]); }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // handle ok button click and filter the list
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

    private void showCustomDateRangeFields(Context context) {
        // Creates a custom dialog for date range input

        // initialize alert dialog
        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(context);
        customDialogBuilder.setTitle("Select Date Range");
        customDialogBuilder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_date_range, null);
        customDialogBuilder.setView(dialogView);

        // set start and end date with calendar
        EditText startDateEditText = dialogView.findViewById(R.id.start_date_edittext);
        EditText endDateEditText = dialogView.findViewById(R.id.end_date_edittext);

        startDateEditText.setOnClickListener(v-> { showCalendar(startDateEditText, context); });
        endDateEditText.setOnClickListener(v-> { showCalendar(endDateEditText, context); });

        customDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            // handle ok button click and retrieve the selected date range from EditText fields
            startDate = startDateEditText.getText().toString();
            endDate = endDateEditText.getText().toString();
            dialogInterface.dismiss();
        });

        customDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        customDialogBuilder.show();
    }


    public void showCalendar(EditText dateEditText, Context context) {

        // create a Calendar instance for the current date
        Calendar selectedDate = Calendar.getInstance();
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // set the date the user selected
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // format the date to only show month and year
            String formattedDate = dateFormat.format(selectedDate.getTime());

            dateEditText.setText(formattedDate);
        },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }

    private void collectUserInput(Context context, String filterOption) {
        // create a custom dialog for collecting user input
        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(context);
        customDialogBuilder.setTitle("Enter " + filterOption);
        customDialogBuilder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // create an EditText to allow the user to enter input
        final EditText editText = new EditText(context);
        customDialogBuilder.setView(editText);

        // handle ok button click and store the entered input in variable
        customDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            // store in keyword if user wants to filter by keyword
            if (filterOption.equals("Description Keyword")) {
                keyword = editText.getText().toString();
                this.itemsByFilter = findItemsWithDescriptionKeyword(keyword);

//                // send bundle with the item name, which is the database key
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("items", this.itemsByFilter);
//
//                // Create a new instance of the TargetFragment
//                filteredItemsFragment fif = new filteredItemsFragment();
//                fif.setArguments(bundle); // Set the data bundle
//
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                navController.navigate(R.id.filteredItemsFragment, bundle);



                // check if items are fetched correctly - OK
//                for (Item item : itemsByFilter){
//                    System.out.println(item.getDescription());
//                }
            }

            // store in make if user wants to filter by make
            else if (filterOption.equals("Make")) {
                make = editText.getText().toString();
//                findItemsWithMake(make);
            }
        });

        customDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        customDialogBuilder.show();
    }

    ArrayList<Item> itemsWithKeyword = new ArrayList<Item>();
    private ArrayList<Item> findItemsWithDescriptionKeyword(String descriptionKeyword){
        for (Item item : existingItems){
            if (item.getDescription().contains(descriptionKeyword)) {
                itemsWithKeyword.add(item);
            }
        }
        return itemsWithKeyword;
    }

    // fetches list of items from the home page
    public void getExistingItems(ArrayList<Item> existingItems){
        this.existingItems = existingItems;
    }

}
