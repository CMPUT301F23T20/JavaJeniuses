package com.example.inventorymanager.ui.sort;

import com.example.inventorymanager.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class ItemSort {

    public void sortItems(ArrayList<Item> items, String sortBy, boolean ascending) {

        // initialize a comparator to specify the sorting order
        Comparator<Item> comparator = null;

        // check the sortBy parameter to determine the sorting criteria
        switch (sortBy) {
            case "Date":
                comparator = (item1, item2) -> {
                    // convert the date strings to actual dates for comparison
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date1 = dateFormat.parse(item1.getPurchaseDate());
                        Date date2 = dateFormat.parse(item2.getPurchaseDate());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                };
                break;
            case "Description":
                comparator = Comparator.comparing(Item::getDescription);
                break;
            case "Make":
                comparator = Comparator.comparing(Item::getMake);
                break;
            case "Estimated Value":
                comparator = (item1, item2) -> {
                    // convert the estimated value strings to doubles for comparison
                    double value1 = Double.parseDouble(item1.getEstimatedValue().substring(1));
                    double value2 = Double.parseDouble(item2.getEstimatedValue().substring(1));
                    return Double.compare(value1, value2);
                };
                break;
            default:
                // handle invalid sortBy value
                break;
        }

        // check if a valid comparator was created
        if (comparator != null) {
            // reverse the sorting order if descending order
            if (!ascending) {
                comparator = comparator.reversed();
            }

            // sort the items list
            items.sort(comparator);
        }
}}
