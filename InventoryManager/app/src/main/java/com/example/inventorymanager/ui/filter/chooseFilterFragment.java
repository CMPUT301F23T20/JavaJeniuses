package com.example.inventorymanager.ui.filter;

import static com.google.common.collect.Iterables.size;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.R;
import com.example.inventorymanager.Tag;
import com.example.inventorymanager.databinding.ChooseFilterBinding;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Manages the screen that enables users to choose filtering conditions.
 * Users may choose filters based on the date the item was purchased, the make of the item, or keywords from the item's description.
 * @author Isaac Joffe, David Onchuru, Sumaiya Salsabil
 * @see filteredItemsFragment
 */
public class chooseFilterFragment extends Fragment {
    private ArrayList<Item> items;
    private ChooseFilterBinding binding;

    /**
     * Provides the user interface of the fragment.
     * Displays filtering information and enables users to change these conditions.
     * @param inflater The object used to inflate views as required.
     * @param container The parent view of the fragment.
     * @param savedInstanceState The previous state of the fragment; not used in this fragment.
     * @return The root of the view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ChooseFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // unpack all items sent to this fragment
        items = getArguments().getParcelableArrayList("items");

        // Bind UI elements to variables
        EditText descriptionKeywordEditText = binding.descriptionKeywordEditText;
        EditText makeKeywordEditText = binding.makeKeywordEditText;
        EditText tagKeywordEditText = binding.tagKeywordEditText;
        EditText startDateEditText = binding.startDateEditText;
        EditText endDateEditText = binding.endDateEditText;
        Button searchButton = binding.searchButton;

        // calendar dialog pops up if start or end date is clicked
        startDateEditText.setOnClickListener(v-> { showCalendar(startDateEditText); });
        endDateEditText.setOnClickListener(v-> { showCalendar(endDateEditText); });

        // search through items with the selected keywords
        searchButton.setOnClickListener(v -> {
            // fetch keywords
            String description = (descriptionKeywordEditText.getText().toString()).toLowerCase();
            String make = makeKeywordEditText.getText().toString().toLowerCase();
            String tag = tagKeywordEditText.getText().toString().toLowerCase();
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            // perform multi-level filtering
            // if user selects date range
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                items = findItemsBetweenDates(startDate, endDate, items);
            }

            // if user selects make
            if (!make.isEmpty()) {
                items = findItemsWithMake(make, items);
            }

            // if user selects description keyword
            if (!description.isEmpty()) {
                items = findItemsWithDescriptionKeyword(description, items);
            }

            // if user selects tag keyword
            if (!tag.isEmpty()) {
                items = findItemsWithTag(tag, items);
                // if item not already in list, add item to filtered item list
            }

            // pack list of filtered items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", items);

            // navigate to filtered items screen with packed list
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.filteredItemsFragment, bundle);

        });

        return root;
    }

    /**
     * Displays a calendar for the user to select dates to filter by.
     * @param dateEditText The View associated with the data corresponding to this calendar.
     */
    public void showCalendar(EditText dateEditText) {

        // create a Calendar instance for the current date
        Calendar selectedDate = Calendar.getInstance();
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth); // set the date the user selected
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // format the date to only show month and year
            String formattedDate = dateFormat.format(selectedDate.getTime());

            dateEditText.setText(formattedDate);
        },
                year, month, dayOfMonth
        );
        // Set the maximum date to the current date to prevent future dates
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Populates a new list with all items containing the desired keyword.
     */
    public ArrayList<Item> findItemsWithDescriptionKeyword(String description, ArrayList<Item> itemsToFilter) {
        ArrayList<Item> itemsWithKeyword = new ArrayList<>();
        for (Item item : itemsToFilter){
            if (item.getDescription().toLowerCase().contains(description)) {
                itemsWithKeyword.add(item);
            }
        }
        return itemsWithKeyword;
    }

    /**
     * Populate a new list with all items containing the desired make.
     */
    public ArrayList<Item> findItemsWithMake(String make, ArrayList<Item> itemsToFilter){
        ArrayList<Item> itemsWithMake = new ArrayList<>();
        for (Item item : itemsToFilter){
            if (item.getMake().toLowerCase().contains(make)) {
                itemsWithMake.add(item);
            }
        }
        return itemsWithMake;
    }

    /**
     * Populate a new list with all items containing the desired tag.
     */
    public ArrayList<Item> findItemsWithTag(String tag, ArrayList<Item> itemsToFilter) {
        ArrayList<Item> itemsWithTag = new ArrayList<>();
        for (Item item : itemsToFilter) {
            if (item.hasTag()) {
                ArrayList<Tag> itemTags = new ArrayList<>();
                itemTags = item.getTags();
                for (Tag itemTag : itemTags) {
                    if (itemTag.getText().toLowerCase().contains(tag.toLowerCase())) {
                        itemsWithTag.add(item);
                        break;
                    }
                }
            }
        }
        return itemsWithTag;
    }

    /**
     * Find all items that fall within certain dates.
     */
    public ArrayList<Item> findItemsBetweenDates(String startDate, String endDate, ArrayList<Item> itemsToFilter){
        ArrayList<Item> itemsBetweenDates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsedStartDate = dateFormat.parse(startDate);
            Date parsedEndDate = dateFormat.parse(endDate);

            for (Item item : itemsToFilter) {
                Date itemDate = dateFormat.parse(item.getPurchaseDate());

                if ((itemDate.equals(parsedStartDate)|| itemDate.after(parsedStartDate)) &&
                        (itemDate.equals(parsedEndDate) || itemDate.before(parsedEndDate))) {
                    itemsBetweenDates.add(item);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return itemsBetweenDates;
    }

    /**
     * Destroys the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}