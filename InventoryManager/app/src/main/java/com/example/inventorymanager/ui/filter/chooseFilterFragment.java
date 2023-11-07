package com.example.inventorymanager.ui.filter;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;

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

            // TODO: Sumaiya
            // if user selects date range
//            if (!startDate.isEmpty() && !endDate.isEmpty()){
//                filteredItems = findItemsBetweenDates(startDate, endDate)
//            }

            // if user entered description and make, filter by both
            if (!description.isEmpty() && !make.isEmpty()){
                filteredItems = findItemsWithDescriptionKeyword(description, items);
                filteredItems = findItemsWithMake(make, filteredItems);
            }
            // if user entered make but not description, filter by make
            else if (!make.isEmpty() && description.isEmpty()){
                filteredItems = findItemsWithMake(make, items);
            }
            // if user entered description but not make, filter by description
            else if (!description.isEmpty() && make.isEmpty()){
                filteredItems = findItemsWithDescriptionKeyword(description, items);
            }

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

    /**
     * Populates a new list with all items containing our desired keyword
     */
    ArrayList<Item> itemsWithKeyword = new ArrayList<Item>();
    private ArrayList<Item> findItemsWithDescriptionKeyword(String description, ArrayList<Item> itemsToFilter){
        for (Item item : itemsToFilter){
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
    private ArrayList<Item> findItemsWithMake(String make, ArrayList<Item> itemsToFilter){
        for (Item item : itemsToFilter){
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
    private ArrayList<Item> findItemsBetweenDates(Date startDate, Date endDate){
        for (Item item : items){
//            if (item.getPurchaseDate() ) {
//
//            }
        }
        return itemsWithMake;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}