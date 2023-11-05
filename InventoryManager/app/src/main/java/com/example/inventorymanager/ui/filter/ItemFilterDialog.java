package com.example.inventorymanager.ui.filter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventorymanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ItemFilterDialog {

    private String startDate;
    private String endDate;
    private String keyword;
    private String make;

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
        // Creates a custom dialog for collecting user input

        // initialize alert dialog
        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(context);
        customDialogBuilder.setTitle("Enter " + filterOption);
        customDialogBuilder.setCancelable(false); // doesn't cancel when user touches outside bounds

        // create an EditText to allow the user to enter input
        final EditText editText = new EditText(context);
        customDialogBuilder.setView(editText);

        // handle ok button click and store the input in variable
        customDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            // store in keyword if user wants to filter by keyword
            if (filterOption.equals("Description Keyword")) {
                keyword = editText.getText().toString();
            }

            // store in make if user wants to filter by make
            else if (filterOption.equals("Make")) {
            make = editText.getText().toString();
        }});

        customDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        customDialogBuilder.show();
    }
}
