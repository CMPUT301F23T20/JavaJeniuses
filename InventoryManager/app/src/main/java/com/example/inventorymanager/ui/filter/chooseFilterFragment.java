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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.ChooseFilterBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class manages the page(choose_filter.xml) that handles users choosing their filter queries
 */

public class chooseFilterFragment extends Fragment {

    private ArrayList<Item> items;

    private ChooseFilterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = ChooseFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // unpack all items sent to this fragment
        items = getArguments().getParcelableArrayList("items");

        // Bind UI elements to variables
        EditText descriptionKeywordEditText = binding.descriptionKeywordEditText;
        EditText makeKeywordEditText = binding.makeKeywordEditText;
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
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            System.out.println(!make.isEmpty());
            System.out.println(description);

            // perform filter operations
            ArrayList<Item> filteredItems = new ArrayList<Item>();

            // if user selects date range
            if (!startDate.isEmpty() && !endDate.isEmpty()){

                // if item not already in list, add item to filtered item list
                for(Item item: findItemsBetweenDates(startDate, endDate)) {
                    if (!filteredItems.contains(item)) { filteredItems.add(item); }
                }

            }

            // if user selects make
            if (!make.isEmpty()){
                // if item not already in list, add item to filtered item list
                for(Item item: findItemsWithMake(make)) {
                    if (!(filteredItems.contains(item))) { filteredItems.add(item); }
                }

            }

            // if user selects description keyword
            if (!description.isEmpty()){
                // if item not already in list, add item to filtered item list
                for(Item item: findItemsWithDescriptionKeyword(description)) {
                    if (!filteredItems.contains(item)) { filteredItems.add(item); }
                }

            }

            ArrayList<Item> itemsToRemove = new ArrayList<Item>();
            for (Item item: filteredItems) {
                if (!itemsBetweenDates.isEmpty()) {
                    if (!itemsBetweenDates.contains(item)) { itemsToRemove.add(item); }}
                if (!itemsWithMake.isEmpty()) {
                    if (!itemsWithMake.contains(item)) { itemsToRemove.add(item); }}
                if (!itemsWithKeyword.isEmpty()) {
                    if (!itemsWithKeyword.contains(item)) { itemsToRemove.add(item); }}
            }
            filteredItems.removeAll(itemsToRemove);


            System.out.println(filteredItems.size());

            if (filteredItems.size() == 0){
                // take user to a page that says: "No items matching your search query"
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.keywordNotFoundFragment);
            }
            else {
                // pack list of filtered items
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("items", filteredItems);

                // navigate to filtered items screen with packed list
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.filteredItemsFragment, bundle);
            }

        });

        return root;
    }
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

        datePickerDialog.show();
    }

    /**
     * Populates a new list with all items containing our desired keyword
     */

    ArrayList<Item> itemsWithKeyword = new ArrayList<Item>();
    private ArrayList<Item> findItemsWithDescriptionKeyword(String description){
        for (Item item : items){
            if (item.getDescription().toLowerCase().contains(description)) {
                itemsWithKeyword.add(item);
            }
        }
        return itemsWithKeyword;
    }

    /**
     * Populate a new list with all items containing our desired make
     */
    ArrayList<Item> itemsWithMake = new ArrayList<Item>();
    private ArrayList<Item> findItemsWithMake(String make){
        for (Item item : items){
            if (item.getMake().toLowerCase().contains(make)) {
                itemsWithMake.add(item);
            }
        }
        return itemsWithMake;
    }

    /**
     * Find all items that fall within these dates
     */
    ArrayList<Item> itemsBetweenDates = new ArrayList<Item>();
    private ArrayList<Item> findItemsBetweenDates(String startDate, String endDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parsedStartDate = dateFormat.parse(startDate);
            Date parsedEndDate = dateFormat.parse(endDate);

            for (Item item : items) {
                Date itemDate = dateFormat.parse(item.getPurchaseDate());

                if ((itemDate.equals(parsedStartDate)|| itemDate.after(parsedStartDate)) &&
                        (itemDate.equals(parsedEndDate) || itemDate.before(parsedEndDate))) {
                    itemsBetweenDates.add(item);
                }
            }
        } catch (ParseException e) { e.printStackTrace(); }

        return itemsBetweenDates;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}