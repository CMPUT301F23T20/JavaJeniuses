package com.example.inventorymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.inventorymanager.ui.filter.chooseFilterFragment;
import com.example.inventorymanager.Item;

import java.util.ArrayList;

/**
 * Tests the functionality of the filtering functions in chooseFilterFragment
 * Ensures the items are properly filtered on basis of the user's input
 * @author David Onchuru
 */
public class filterItemsTest {
    // defines realistic test data to be used for two different items
    private final String itemName1 = "Computer";
    private final String itemName2 = "Laptop";
    private final String itemName3 = "Tablet";

    private final String purchaseDate1 = "2023-01-01";
    private final String purchaseDate2 = "2023-08-20";
    private final String description1 = "PC used for gaming.";
    private final String description2 = "Laptop used for school.";
    private final String description3 = "Tablet used for school.";
    private final String model1 = "iMac";
    private final String model2 = "Surface Pro";
    private final String make1 = "Apple";
    private final String make2 = "Microsoft";
    private final String serialNumber1 = "1234567890ABCD";
    private final String serialNumber2 = "DCBA0987654321";
    private final String estimatedValue1 = "$2,500.00";
    private final String estimatedValue2 = "$1,399.99";
    private final String comment1 = "Uses high-speed M1 chip.";
    private final String comment2 = "Uses medium-speed Intel chip.";
    chooseFilterFragment chooseFilterFragment = new chooseFilterFragment();

    /**
     * Prepopulates a list of items that will be used to test how the filtering functions
     * @return a list of default items
     */
    private ArrayList<Item> defaultList() {
        ArrayList<Item> items = new ArrayList<Item>();

        Item item1 = new Item(itemName1, purchaseDate1, description1, model1, make1, serialNumber1, estimatedValue1, comment1);
        Item item2 = new Item(itemName2, purchaseDate2, description1, model1, make2, serialNumber1, estimatedValue1, comment1);
        Item item3 = new Item(itemName3, purchaseDate1, description3, model1, make1, serialNumber1, estimatedValue1, comment1);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        return items;
    }

    /**
     * Tests that the filter by description feature works as expected
     */
    @Test
    public void testFilterByDescription() {
        ArrayList<Item> items = defaultList();
        ArrayList<Item> filteredItems = chooseFilterFragment.findItemsWithDescriptionKeyword("gaming", items);
        assertTrue(filteredItems.size() == 2);
    }

    /**
     * Tests that the filter by make feature works as expected
     */
    @Test
    public void testFilterByMake() {
        ArrayList<Item> items = defaultList();
        ArrayList<Item> filteredItems = chooseFilterFragment.findItemsWithMake("micro", items);
        assertEquals(filteredItems.size(), 1);
    }

    /**
     * Tests that the filter by dates feature works as expected
     */
    @Test
    public void testFilterByDates() {
        ArrayList<Item> items = defaultList();
        ArrayList<Item> filteredItems = chooseFilterFragment.findItemsBetweenDates("2023-08-15", "2023-08-25", items);
        assertEquals(filteredItems.size(), 1);
    }

}
