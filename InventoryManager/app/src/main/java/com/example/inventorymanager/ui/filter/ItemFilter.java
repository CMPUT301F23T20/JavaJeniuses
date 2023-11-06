package com.example.inventorymanager.ui.filter;

import com.example.inventorymanager.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemFilter {
    private ArrayList<Item> filteredList;

    public void filterByDateRange(ArrayList<Item> items, String startDate, String endDate){

        filteredList = new ArrayList<Item>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = null;
        Date parsedEndDate = null;

        try {
            if (startDate != null) { parsedStartDate = dateFormat.parse(startDate); }
            if (endDate != null) { parsedEndDate = dateFormat.parse(endDate); }

            for (Item item : items) {
                Date itemDate = dateFormat.parse(item.getPurchaseDate());

                if ((startDate == null || itemDate.after(parsedStartDate)) &&
                        (endDate == null || itemDate.before(parsedEndDate))) {
                    filteredList.add(item);
                }
            }
        } catch (ParseException e) { e.printStackTrace(); }

    }

    public ArrayList<Item> getFilteredList() {
        return filteredList;
    }
}
