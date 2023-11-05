package com.example.inventorymanager;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ItemFilter {

    private String startDate;
    private String endDate;

    public void showFilterDialog(Context context) {

        // options for filtering the items
        String[] filterOptions = {"Date Range", "Description Keyword", "Make"};
        boolean[] selectedOption = new boolean[filterOptions.length];

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set title
        builder.setTitle("Filter Items By: ");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(filterOptions, selectedOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (filterOptions[i].equals("Date Range")) {
                    showCustomDateRangeFields(context);
                    /*Calendar selectedDate = Calendar.getInstance(); // Create a Calendar instance for the current date
                    int year = selectedDate.get(Calendar.YEAR);
                    int month = selectedDate.get(Calendar.MONTH);
                    int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // Set the date the user selected
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Format the date to only show month and year
                    },
                            year, month, dayOfMonth
                    );

                    datePickerDialog.show();*/
                }}
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // skip
                System.out.println("Yay.");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }

    private void showCustomDateRangeFields(Context context) {
        // Create a custom dialog for date range input
        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(context);
        customDialogBuilder.setTitle("Select Date Range");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_date_range, null);
        customDialogBuilder.setView(dialogView);

        EditText startDateEditText = dialogView.findViewById(R.id.start_date_edittext);
        EditText endDateEditText = dialogView.findViewById(R.id.end_date_edittext);

        startDateEditText.setOnClickListener(v-> {
            Calendar selectedDate = Calendar.getInstance(); // Create a Calendar instance for the current date
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // Set the date the user selected
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Format the date to only show month and year
                String formattedDate = dateFormat.format(selectedDate.getTime());

                startDateEditText.setText(formattedDate);
            },
                    year, month, dayOfMonth
            );

            datePickerDialog.show();


        });

        endDateEditText.setOnClickListener(v-> {
            Calendar selectedDate = Calendar.getInstance(); // Create a Calendar instance for the current date
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // Set the date the user selected
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Format the date to only show month and year
                String formattedDate = dateFormat.format(selectedDate.getTime());

                endDateEditText.setText(formattedDate);
            },
                    year, month, dayOfMonth
            );

            datePickerDialog.show();
        });

        customDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
            // Handle OK button click and retrieve the selected date range from EditText fields
            startDate = startDateEditText.getText().toString();
            endDate = endDateEditText.getText().toString();
            dialogInterface.dismiss();
        });

        customDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        customDialogBuilder.show();
    }
}
