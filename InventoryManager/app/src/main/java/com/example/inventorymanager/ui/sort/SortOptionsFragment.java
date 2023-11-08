package com.example.inventorymanager.ui.sort;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.inventorymanager.Item;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentSortOptionsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SortOptionsFragment extends Fragment {

    private ArrayList<Item> items, sortedItems;

    private FragmentSortOptionsBinding binding;
    private boolean ascending;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSortOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // unpack all items sent to this fragment
        items = getArguments().getParcelableArrayList("items");

        // Bind UI elements to variables
        ToggleButton dateButton = binding.sortDateButton;
        ToggleButton descriptionButton = binding.sortDescriptionButton;
        ToggleButton makeButton = binding.sortMakeButton;
        ToggleButton valueButton = binding.sortValueButton;
        ToggleButton ascendingButton = binding.ascendingButton;
        Button doneButton = binding.sortDone;

        // create new list to hold sorted items
        sortedItems = new ArrayList<Item>();
        sortedItems.addAll(items);

        // set ascending default to be true
        ascending = true;

        // if button switched to descending, change ascending to false
        ascendingButton.setOnCheckedChangeListener( (buttonView, isChecked) -> {ascending = false; });

        // create array to hold sorting order
        ArrayList<String> sortList = new ArrayList<String>();

        // add sorting method to array when button clicked
        dateButton.setOnCheckedChangeListener( (buttonView, isChecked) -> { sortList.add("Date"); });
        descriptionButton.setOnCheckedChangeListener( (buttonView, isChecked) -> { sortList.add("Description"); });
        makeButton.setOnCheckedChangeListener( (buttonView, isChecked) -> { sortList.add("Make"); });
        valueButton.setOnCheckedChangeListener( (buttonView, isChecked) -> { sortList.add("Value"); });

        // handle done button click
        doneButton.setOnClickListener( v -> {

            // sort items
            sortItems(sortList);

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", sortedItems);

            // navigate to filtered items screen with packed list
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.sortedItemsFragment, bundle);
        });

        return root;
    }

    private void sortItems(ArrayList<String> sortBys) {

        // create a list of comparators based on sortBys
        ArrayList<Comparator<Item>> comparators = new ArrayList<>();
        for (int i = 0; i < sortBys.size(); i++) {
            String sortBy = sortBys.get(i);
            comparators.add(createComparator(sortBy, ascending));
        }

        // create a composite comparator that performs multisort
        Comparator<Item> multiComparator = (item1, item2) -> {
            for (Comparator<Item> comparator : comparators) {
                int result = comparator.compare(item1, item2);
                if (result != 0) {
                    return result;
                }
            }
            return 0; // items are considered equal if all comparators return 0
        };

        // sort the items list using the composite comparator
        sortedItems.sort(multiComparator);
    }

    private Comparator<Item> createComparator(String sortBy, boolean ascending) {
        Comparator<Item> comparator = null;

        switch (sortBy) {
            case "Date":
                comparator = (item1, item2) -> {
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
            case "Value":
                comparator = (item1, item2) -> {
                    double value1 = Double.parseDouble(item1.getEstimatedValue().replaceAll("[$,]", ""));
                    double value2 = Double.parseDouble(item2.getEstimatedValue().replaceAll("[$,]", ""));
                    return Double.compare(value1, value2);
                };
                break;
            default:
                // handle invalid sortBy value
                break;
        }

        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
        }

        return comparator;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}